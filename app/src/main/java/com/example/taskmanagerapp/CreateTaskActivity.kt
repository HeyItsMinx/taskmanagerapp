package com.example.taskmanagerapp.ui.createtask

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.taskmanagerapp.api.ApiClient
import com.example.taskmanagerapp.databinding.ActivityCreateTaskBinding
import com.example.taskmanagerapp.model.Category
import kotlinx.coroutines.launch
import android.util.Log
import android.widget.ImageButton
import com.example.taskmanagerapp.R

class CreateTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

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

            val taskBody = mapOf(
                "title" to taskName,
                "description" to taskDescription,
                "category_id" to selectedCategory.id
            )

            lifecycleScope.launch {
                try {
                    val response = ApiClient.apiService.createTask(taskBody)
                    if (response.isSuccessful) {
                        Toast.makeText(this@CreateTaskActivity, "Task successfully created!", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("CreateTaskActivity", "Failed Creating Task: $errorBody")
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
