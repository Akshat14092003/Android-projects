package com.example.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private OnTaskInteractionListener listener;
    private Context context;

    public interface OnTaskInteractionListener {
        void onEditTask(int position);
        void onDeleteTask(int position);
        void onTaskCompletedChanged(int position, boolean isCompleted);
    }

    public TaskAdapter(List<Task> tasks, OnTaskInteractionListener listener, Context context) {
        this.tasks = tasks;
        this.listener = listener;
        this.context = context; // Initialize context
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.title.setText(task.getTitle());
        holder.toggleSwitch.setChecked(task.isCompleted());
        holder.toggleSwitch.setText(task.isCompleted() ? "Completed" : "Active");

        // Set a tag to identify the position in the listener
        holder.toggleSwitch.setTag(position);

        holder.toggleSwitch.setOnCheckedChangeListener(null); // Clear listener to prevent unwanted triggering
        holder.toggleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int pos = (int) buttonView.getTag(); // Get the position from the tag

            // Update the task completion status
            tasks.get(pos).setCompleted(isChecked);

            // Notify the listener
            listener.onTaskCompletedChanged(pos, isChecked);

            // Update the text based on the checked state
            holder.toggleSwitch.setText(isChecked ? "Completed" : "Active");
        });

        holder.editButton.setOnClickListener(v -> listener.onEditTask(position));

        holder.deleteButton.setOnClickListener(v -> {
            showDeleteConfirmationDialog(position);
        });
    }

    private void showDeleteConfirmationDialog(final int position) {
        if (context == null) {
            return; // Ensure context is not null
        }

        new AlertDialog.Builder(context)
                .setTitle("Delete Task")
                .setMessage("Do you really want to delete this task?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    listener.onDeleteTask(position);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        Switch toggleSwitch;
        TextView title;
        ImageButton editButton;
        ImageButton deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            toggleSwitch = itemView.findViewById(R.id.toggleSwitch);
            title = itemView.findViewById(R.id.title);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
