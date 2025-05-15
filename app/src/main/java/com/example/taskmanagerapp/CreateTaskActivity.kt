package com.example.taskmanagerapp.ui.createtask

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanagerapp.databinding.ActivityCreateTaskBinding
import com.example.taskmanagerapp.model.Category
import com.example.taskmanagerapp.model.Task

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
                else -> Category(3, "Regular") // fallback
            }

            if (taskName.isEmpty()) {
                Toast.makeText(this, "Please enter task name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newTask = Task(
                id = 0, // should be assigned by backend/db
                title = taskName,
                category = selectedCategory,
                description = taskDescription.takeIf { it.isNotEmpty() },
                done = false,
                updated_at = "" // current timestamp
            )

            // TODO: Save newTask to DB or API here
            Toast.makeText(this, "Task saved: $newTask", Toast.LENGTH_LONG).show()
            finish() // Close this activity after saving
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }
}
