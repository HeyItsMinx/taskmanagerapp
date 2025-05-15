package com.example.taskmanagerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagerapp.R
import com.example.taskmanagerapp.model.Task
import android.util.Log

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


    private fun formatDate(isoDate: String): String {
        return try {
            val parser = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault())
            parser.timeZone = java.util.TimeZone.getTimeZone("UTC")
            val date = parser.parse(isoDate)

            val formatter = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault())
            "Updated: ${formatter.format(date!!)}"
        } catch (e: Exception) {
            "Updated: $isoDate"
        }
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.title.text = task.title
        holder.category.text = task.category.name
        holder.updated.text = formatDate(task.updated_at)
        holder.description.text = task.description ?: ""

        holder.btnEdit.setOnClickListener { onEdit(task) }
        holder.btnDelete.setOnClickListener { onDelete(task) }

        // Toggle done / undone
        holder.btnDone.text = if (task.done) "Done" else "Finish"
        holder.btnDone.setOnClickListener { onDone(task) }

        if (task.done) {
            val gray = ContextCompat.getColor(holder.itemView.context, android.R.color.darker_gray)
            holder.title.setTextColor(gray)
            holder.category.setTextColor(gray)
            holder.updated.setTextColor(gray)
            holder.description.setTextColor(gray)

            holder.btnDone.alpha = 0.4f
            holder.btnEdit.visibility = View.GONE
            holder.btnDelete.visibility = View.GONE

            holder.btnEdit.isEnabled = false
            holder.btnDelete.isEnabled = false
        } else {
            val black = ContextCompat.getColor(holder.itemView.context, android.R.color.black)
            val gray = ContextCompat.getColor(holder.itemView.context, android.R.color.darker_gray)

            holder.title.setTextColor(black)
            holder.category.setTextColor(gray)
            holder.updated.setTextColor(gray)
            holder.description.setTextColor(black)

            holder.btnEdit.alpha = 1f
            holder.btnDone.alpha = 1f
            holder.btnDelete.alpha = 1f

            holder.btnEdit.isEnabled = true
            holder.btnDelete.isEnabled = true

            holder.btnEdit.visibility = View.VISIBLE
            holder.btnDelete.visibility = View.VISIBLE
        }
    }


    override fun getItemCount() = tasks.size

}
