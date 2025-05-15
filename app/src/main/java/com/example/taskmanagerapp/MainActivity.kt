package com.example.taskmanagerapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.example.taskmanagerapp.api.ApiClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var tvAllTasksCount: TextView
    private lateinit var tvImportantTasksCount: TextView
    private lateinit var tvUrgentTasksCount: TextView
    private lateinit var tvRegularTasksCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Apply status bar padding to root layout
        val rootView = findViewById<ConstraintLayout>(R.id.mainContainer)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val topInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.updatePadding(top = topInset)
            insets
        }

        // Bind views
        tvAllTasksCount = findViewById(R.id.tvAllTasksCount)
        tvImportantTasksCount = findViewById(R.id.tvImportantTasksCount)
        tvUrgentTasksCount = findViewById(R.id.tvUrgentTasksCount)
        tvRegularTasksCount = findViewById(R.id.tvRegularTasksCount)

        // Fetch and count tasks
        fetchTaskCounts()

        // Click listeners to open TaskListActivity with category filter
        findViewById<androidx.cardview.widget.CardView>(R.id.cardAllTasks).setOnClickListener {
            openTaskListActivity(null) // Show all tasks
        }

        findViewById<androidx.cardview.widget.CardView>(R.id.cardImportantTasks).setOnClickListener {
            openTaskListActivity("Important")
        }

        findViewById<androidx.cardview.widget.CardView>(R.id.cardUrgentTasks).setOnClickListener {
            openTaskListActivity("Urgent")
        }

        findViewById<androidx.cardview.widget.CardView>(R.id.cardRegularTasks).setOnClickListener {
            openTaskListActivity("Regular")
        }
    }

    private fun openTaskListActivity(category: String?) {
        val intent = Intent(this, com.example.taskmanagerapp.ui.tasklist.TaskListActivity::class.java)
        if (!category.isNullOrEmpty()) {
            intent.putExtra("category_name", category)
        }
        startActivity(intent)
    }

    private fun fetchTaskCounts() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getTasks()
                val allTasks = response.data

                val importantCount = allTasks.count { it.category.name.equals("Important", ignoreCase = true) }
                val urgentCount = allTasks.count { it.category.name.equals("Urgent", ignoreCase = true) }
                val regularCount = allTasks.count { it.category.name.equals("Regular", ignoreCase = true) }
                val totalCount = allTasks.size

                updateTaskCounts(importantCount, urgentCount, regularCount, totalCount)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateTaskCounts(important: Int, urgent: Int, regular: Int, total: Int) {
        tvImportantTasksCount.text = important.toString()
        tvUrgentTasksCount.text = urgent.toString()
        tvRegularTasksCount.text = regular.toString()
        tvAllTasksCount.text = total.toString()
    }
}
