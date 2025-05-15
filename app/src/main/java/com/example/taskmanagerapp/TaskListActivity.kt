package com.example.taskmanagerapp.ui.tasklist

import android.app.AlertDialog
import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageButton
import android.content.Intent
import android.widget.TextView
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
import com.example.taskmanagerapp.ui.createtask.CreateTaskActivity
import android.widget.Toast
import com.example.taskmanagerapp.model.Category
import com.example.taskmanagerapp.model.Task
import com.example.taskmanagerapp.dummy.DummyData
import kotlinx.coroutines.launch

class TaskListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskListBinding
    private lateinit var tvCategory: TextView
    private lateinit var tvTaskCount: TextView
    private lateinit var btnAdd: ImageButton

    private val tasks = mutableListOf<Task>()
    private var categoryFilter: String? = null

    // Toggle dummy data
    private val useDummyData = false



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

        // Access custom views
        tvCategory = findViewById(R.id.tvCategory)
        tvTaskCount = findViewById(R.id.tvTaskCount)
        btnAdd = findViewById(R.id.btnAdd)

        // Apply underline to task count
        tvTaskCount.paintFlags = tvTaskCount.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        // Get category filter from intent extras
        categoryFilter = intent.getStringExtra("category_name")

        tvCategory.setOnClickListener {
            showCategoryPicker()
        }

        // Setup add task button click
        btnAdd.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            startActivity(intent)
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
                val categoryNames = mutableListOf("All.") // add All option
                val nameToIdMap = mutableMapOf<String, String?>(Pair("All.", null))

                categoryResponse.data.forEach {
                    categoryNames.add(it.name)
                    nameToIdMap[it.name] = it.name
                }

                val selectedIndex = categoryNames.indexOf(categoryFilter ?: "All.")

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

                updateToolbar(categoryFilter, tasks.size)

                binding.recyclerViewTasks.adapter = TaskAdapter(tasks,
                    onEdit = { /* TODO */ },
                    onDone = { task ->
                        lifecycleScope.launch {
                            if (!useDummyData) {
                                ApiClient.apiService.finishTask(task.id)
                            }
                            task.done = true
                            binding.recyclerViewTasks.adapter?.notifyDataSetChanged()
                            updateToolbar(categoryFilter, tasks.size)
                        }
                    },
                    onDelete = { task ->
                        lifecycleScope.launch {
                            if (!useDummyData) {
                                ApiClient.apiService.deleteTask(task.id)
                            }
                            tasks.remove(task)
                            binding.recyclerViewTasks.adapter?.notifyDataSetChanged()
                            updateToolbar(categoryFilter, tasks.size)
                        }
                    }
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}
