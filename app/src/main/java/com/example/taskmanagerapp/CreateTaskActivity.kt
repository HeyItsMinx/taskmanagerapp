package com.example.taskmanagerapp.ui.createtask

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.taskmanagerapp.api.ApiClient
import com.example.taskmanagerapp.databinding.ActivityCreateTaskBinding
import com.example.taskmanagerapp.model.Category
import com.example.taskmanagerapp.model.TaskRequest
import kotlinx.coroutines.launch
import android.util.Log

class CreateTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            val taskName = binding.etTaskName.text.toString().trim()
            val taskDescription = binding.etTaskDescription.text.toString().trim()

            val selectedCategory = when (binding.rgTaskType.checkedRadioButtonId) {
                binding.rbImportant.id -> Category(1, "Important")
                binding.rbUrgent.id -> Category(2, "Urgent")
                binding.rbRegular.id -> Category(3, "Regular")
                else -> Category(3, "Regular")
            }

            if (taskName.isEmpty()) {
                Toast.makeText(this, "Please enter task name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🔁 Prepare request body
            val taskRequest = TaskRequest(
                title = taskName,
                description = taskDescription,
                category_id = selectedCategory.id
            )

            // 🌐 Call API
            lifecycleScope.launch {
                try {
                    val response = ApiClient.apiService.createTask(taskRequest)
                    if (response.isSuccessful) {
                        Toast.makeText(this@CreateTaskActivity, "Task successfully created!", Toast.LENGTH_SHORT).show()
                        finish() // Close
                    } else {
                        Toast.makeText(this@CreateTaskActivity, "Failed to create task", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("CreateTaskActivity", "API call failed", e)
                    Toast.makeText(this@CreateTaskActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }
}
