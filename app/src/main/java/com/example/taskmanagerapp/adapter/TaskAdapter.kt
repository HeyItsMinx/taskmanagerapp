package com.example.taskmanagerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagerapp.R
import com.example.taskmanagerapp.model.Task

class TaskAdapter(
    private val tasks: List<Task>,
    private val onEdit: (Task) -> Unit,
    private val onDone: (Task) -> Unit,
    private val onDelete: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.taskTitle)
        val category = view.findViewById<TextView>(R.id.taskCategory)
        val updated = view.findViewById<TextView>(R.id.taskUpdated)
        val description = view.findViewById<TextView>(R.id.taskDescription)
        val btnEdit = view.findViewById<Button>(R.id.btnEdit)
        val btnDone = view.findViewById<Button>(R.id.btnDone)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.category.text = task.category.name
        holder.updated.text = task.updated_at
        holder.description.text = task.description ?: ""

        holder.btnEdit.setOnClickListener { onEdit(task) }
        holder.btnDone.setOnClickListener { onDone(task) }
        holder.btnDelete.setOnClickListener { onDelete(task) }

        if (task.done) {
            holder.btnEdit.isEnabled = false
            holder.btnDelete.isEnabled = false
        }
    }

    override fun getItemCount() = tasks.size
}
