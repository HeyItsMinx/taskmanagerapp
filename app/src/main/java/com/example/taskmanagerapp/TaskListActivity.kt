package com.example.taskmanagerapp.ui.tasklist

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanagerapp.R
import com.example.taskmanagerapp.adapter.TaskAdapter
import com.example.taskmanagerapp.api.ApiClient
import com.example.taskmanagerapp.databinding.ActivityTaskListBinding
import com.example.taskmanagerapp.dummy.DummyData
import com.example.taskmanagerapp.model.Task
import com.example.taskmanagerapp.ui.createtask.CreateTaskActivity
import kotlinx.coroutines.launch

class TaskListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskListBinding
    private lateinit var tvCategory: TextView
    private lateinit var tvTaskCount: TextView
    private lateinit var btnAdd: ImageButton

    private val tasks = mutableListOf<Task>()
    private var categoryFilter: String? = null
    private val useDummyData = false

    // ✅ Register Activity Result Launcher
    private val createTaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            fetchTasks()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { view, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.updatePadding(top = statusBarHeight)
            insets
        }

        tvCategory = findViewById(R.id.tvCategory)
        tvTaskCount = findViewById(R.id.tvTaskCount)
        btnAdd = findViewById(R.id.btnAdd)

        tvTaskCount.paintFlags = tvTaskCount.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        categoryFilter = intent.getStringExtra("category_name")

        tvCategory.setOnClickListener {
            showCategoryPicker()
        }

        btnAdd.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            createTaskLauncher.launch(intent) // ✅ use launcher instead of startActivity
        }

        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        fetchTasks()
    }

    private fun updateToolbar(categoryName: String?, taskCount: Int) {
        tvCategory.text = categoryName?.takeIf { it.isNotBlank() } ?: "All."
        tvTaskCount.text = taskCount.toString()
    }

    private fun showCategoryPicker() {
        lifecycleScope.launch {
            try {
                val categoryResponse = ApiClient.apiService.getCategories()
                val categoryNames = mutableListOf("All")
                val nameToIdMap = mutableMapOf<String, String?>(Pair("All", null))

                categoryResponse.data.forEach {
                    categoryNames.add(it.name)
                    nameToIdMap[it.name] = it.name
                }

                val selectedIndex = categoryNames.indexOf(categoryFilter ?: "All")

                AlertDialog.Builder(this@TaskListActivity)
                    .setTitle("Choose Category")
                    .setSingleChoiceItems(categoryNames.toTypedArray(), selectedIndex) { dialog, which ->
                        val selectedName = categoryNames[which]
                        categoryFilter = nameToIdMap[selectedName]
                        fetchTasks()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@TaskListActivity, "Failed to load categories", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchTasks() {
        lifecycleScope.launch {
            try {
                val allTasks = if (useDummyData) {
                    DummyData.tasks
                } else {
                    ApiClient.apiService.getTasks().data
                }

                val filteredTasks = categoryFilter?.let { name ->
                    allTasks.filter { it.category.name == name }
                } ?: allTasks

                tasks.clear()
                tasks.addAll(filteredTasks)

                tasks.sortWith(compareBy({ it.done }, { it.updated_at }))
                updateToolbar(categoryFilter, tasks.size)

                binding.recyclerViewTasks.adapter = TaskAdapter(tasks,
                    onEdit = { /* TODO */ },
                    onDone = { task ->
                        lifecycleScope.launch {
                            val newDoneState = !task.done // Toggle
                            val body = mapOf("done" to newDoneState)

                            val response = ApiClient.apiService.updateTask(task.id, body)
                            if (response.isSuccessful) {
                                task.done = newDoneState
                                // Re-sort to move done tasks to bottom
                                tasks.sortWith(compareBy({ it.done }, { it.updated_at }))
                                binding.recyclerViewTasks.adapter?.notifyDataSetChanged()
                                updateToolbar(categoryFilter, tasks.size)
                            } else {
                                Toast.makeText(
                                    this@TaskListActivity,
                                    "Failed to update task",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    onDelete = { task ->
                        lifecycleScope.launch {
                            if (!useDummyData) {
                                val response = ApiClient.apiService.deleteTask(task.id)
                                if (response.isSuccessful) {
                                    tasks.remove(task)
                                    binding.recyclerViewTasks.adapter?.notifyDataSetChanged()
                                    updateToolbar(categoryFilter, tasks.size)
                                    Toast.makeText(this@TaskListActivity, "Task deleted", Toast.LENGTH_SHORT).show()
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    Toast.makeText(this@TaskListActivity, "Failed to delete task: $errorBody", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                tasks.remove(task)
                                binding.recyclerViewTasks.adapter?.notifyDataSetChanged()
                                updateToolbar(categoryFilter, tasks.size)
                            }
                        }
                    }
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@TaskListActivity, "Failed to load tasks", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
