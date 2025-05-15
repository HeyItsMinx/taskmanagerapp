package com.example.taskmanagerapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import android.view.View

// Data class untuk kategori
data class Category(val id: Int, val name: String)

// Retrofit interface
interface ApiService {
    @GET("category")
    fun getCategories(): Call<List<Category>>
}

class EntryTaskActivity : AppCompatActivity() {

    private lateinit var TaskName: EditText
    private lateinit var Description: EditText
    private lateinit var Categories: RadioGroup
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_task)

        // bind views
        TaskName   = findViewById(R.id.TaskName)
        Description= findViewById(R.id.Description)
        Categories = findViewById(R.id.Categories)
        btnSave      = findViewById(R.id.btnSave)
        btnCancel    = findViewById(R.id.btnCancel)

        // setup Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://your-api-base-url.com/") // ganti sesuai endpoint-mu
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ApiService::class.java)

        // load categories dari API
        loadCategories()

        // tombol Save
        btnSave.setOnClickListener {
            val name =TaskName.text.toString().trim()
            val desc = Description.text.toString().trim()
            val checkedId = Categories.checkedRadioButtonId
            if (name.isEmpty() || desc.isEmpty() || checkedId == -1) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val selectedRb = findViewById<RadioButton>(checkedId)
            val categoryName = selectedRb.text.toString()

            // TODO: Insert ke database di sini
            // misal: MyDatabase.getInstance(this).taskDao().insert(Task(...))

            Toast.makeText(this, "Task saved!", Toast.LENGTH_SHORT).show()
            finish() // atau Intent kembali ke MainActivity
        }

        // tombol Cancel
        btnCancel.setOnClickListener {
            // kembali ke MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
    }

    private fun loadCategories() {
        api.getCategories().enqueue(object: Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList<Category>()
                    populateRadioGroup(list)
                } else {
                    Toast.makeText(this@EntryTaskActivity, "Failed to load categories", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Toast.makeText(this@EntryTaskActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun populateRadioGroup(categories: List<Category>) {
        Categories.removeAllViews()
        categories.forEach { cat ->
            val rb = RadioButton(this).apply {
                id = View.generateViewId()
                text = cat.name
            }
            Categories.addView(rb)
        }
    }
}