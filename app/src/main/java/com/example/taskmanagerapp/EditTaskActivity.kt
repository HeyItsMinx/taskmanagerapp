package com.example.taskmanagerapp.ui.edittask

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.example.taskmanagerapp.R
import com.example.taskmanagerapp.api.ApiClient
import com.example.taskmanagerapp.databinding.ActivityCreateTaskBinding
import kotlinx.coroutines.launch

class EditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTaskBinding
    private var taskId: Int = -1
    private var selectedCategoryId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        taskId = intent.getIntExtra("task_id", -1)
        val title = intent.getStringExtra("title") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val categoryName = intent.getStringExtra("category_name") ?: "Regular"

        binding.etTaskName.setText(title)
        binding.etTaskDescription.setText(description)

        // Map the category to RadioButton
        when (categoryName.lowercase()) {
            "important" -> {
                binding.rbImportant.isChecked = true
                selectedCategoryId = 1
            }
            "urgent" -> {
                binding.rbUrgent.isChecked = true
                selectedCategoryId = 2
            }
            else -> {
                binding.rbRegular.isChecked = true
                selectedCategoryId = 3
            }
        }

        binding.rgTaskType.setOnCheckedChangeListener { _, checkedId ->
            selectedCategoryId = when (checkedId) {
                binding.rbImportant.id -> 1
                binding.rbUrgent.id -> 2
                else -> 3
            }
        }

        binding.btnSave.setOnClickListener {
            val updatedTitle = binding.etTaskName.text.toString().trim()
            val updatedDescription = binding.etTaskDescription.text.toString().trim()

            val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
            ViewCompat.setOnApplyWindowInsetsListener(toolbar) { view, insets ->
                val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                view.updatePadding(top = statusBarHeight)
                insets
            }

            if (updatedTitle.isEmpty()) {
                Toast.makeText(this, "Please enter task name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updateBody = mapOf(
                "title" to updatedTitle,
                "description" to updatedDescription,
                "category_id" to selectedCategoryId
            )

            lifecycleScope.launch {
                try {
                    val response = ApiClient.apiService.updateTask(taskId, updateBody)
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditTaskActivity, "Task updated!", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@EditTaskActivity, "Update failed", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@EditTaskActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }
}

