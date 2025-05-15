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
import java.text.SimpleDateFormat
import java.util.*

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
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            parser.timeZone = TimeZone.getTimeZone("UTC")
            val date = parser.parse(isoDate)
            val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
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

        // Change text on button
        holder.btnDone.text = if (task.done) "Undone" else "Done"

        // Set button click events
        holder.btnEdit.setOnClickListener { onEdit(task) }
        holder.btnDone.setOnClickListener { onDone(task) }
        holder.btnDelete.setOnClickListener { onDelete(task) }

        // Apply UI state based on done status
        if (task.done) {
            val grayColor = ContextCompat.getColor(holder.itemView.context, android.R.color.darker_gray)

            // Grey out text
            holder.title.setTextColor(grayColor)
            holder.category.setTextColor(grayColor)
            holder.updated.setTextColor(grayColor)
            holder.description.setTextColor(grayColor)

            // Hide edit and delete buttons
            holder.btnEdit.visibility = View.GONE
            holder.btnDelete.visibility = View.GONE

            // Update Done button to be final state
            holder.btnDone.text = "Done"
            holder.btnDone.isEnabled = false
            holder.btnDone.alpha = 0.5f
        } else {
            val black = ContextCompat.getColor(holder.itemView.context, android.R.color.black)
            val gray = ContextCompat.getColor(holder.itemView.context, android.R.color.darker_gray)

            holder.title.setTextColor(black)
            holder.category.setTextColor(gray)
            holder.updated.setTextColor(gray)
            holder.description.setTextColor(black)

            // Show buttons
            holder.btnEdit.visibility = View.VISIBLE
            holder.btnDelete.visibility = View.VISIBLE

            // Enable Done button
            holder.btnDone.text = "Done"
            holder.btnDone.isEnabled = true
            holder.btnDone.alpha = 1f
        }

    }

    override fun getItemCount() = tasks.size
}
