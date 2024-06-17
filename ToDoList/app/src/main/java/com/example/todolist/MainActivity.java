package com.example.todolist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskInteractionListener {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> tasks;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private static final String TASKS_KEY = "tasks_key";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("tasks_prefs", MODE_PRIVATE);
        gson = new Gson();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasks = loadTasks();
        if (tasks == null) {
            tasks = new ArrayList<>();
        }

        adapter = new TaskAdapter(tasks, this, this); // Pass context as this
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> showTaskDialog(null, -1));
    }

    private List<Task> loadTasks() {
        String tasksJson = sharedPreferences.getString(TASKS_KEY, null);
        if (tasksJson != null) {
            Type type = new TypeToken<List<Task>>() {}.getType();
            return gson.fromJson(tasksJson, type);
        }
        return null;
    }

    private void saveTasks() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String tasksJson = gson.toJson(tasks);
        editor.putString(TASKS_KEY, tasksJson);
        editor.apply();
    }

    private void showTaskDialog(@Nullable Task task, int position) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_task, null);

        EditText titleInput = view.findViewById(R.id.titleInput);
        EditText descriptionInput = view.findViewById(R.id.descriptionInput);

        if (task != null) {
            titleInput.setText(task.getTitle());
            descriptionInput.setText(task.getDescription());
        }

        new AlertDialog.Builder(this)
                .setTitle(task == null ? "Add Task" : "Edit Task")
                .setView(view)
                .setPositiveButton(task == null ? "Add" : "Update", (dialog, which) -> {
                    String title = titleInput.getText().toString();
                    String description = descriptionInput.getText().toString();

                    if (task == null) {
                        tasks.add(new Task(title, description, false));
                    } else {
                        task.setTitle(title);
                        task.setDescription(description);
                        tasks.set(position, task);
                    }

                    adapter.notifyDataSetChanged();
                    saveTasks();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onEditTask(int position) {
        showTaskDialog(tasks.get(position), position);
    }

    @Override
    public void onDeleteTask(int position) {
        tasks.remove(position);
        adapter.notifyItemRemoved(position);
        saveTasks();
    }

    @Override
    public void onTaskCompletedChanged(int position, boolean isCompleted) {
        tasks.get(position).setCompleted(isCompleted);
        adapter.notifyItemChanged(position);
        saveTasks();
    }
}
