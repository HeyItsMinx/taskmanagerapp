package com.example.taskmanagerapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.api_result)
        val button = findViewById<Button>(R.id.call_api_button)

        button.setOnClickListener {
            val request = Request.Builder()
                .url("http://10.0.2.2:3000/") // Use 10.0.2.2 for emulator
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        textView.text = "Failed: ${e.message}"
                    }
                    Log.e("API_CALL", "Failed: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseText = response.body?.string()
                    runOnUiThread {
                        if (response.isSuccessful) {
                            textView.text = "Response: $responseText"
                        } else {
                            textView.text = "Error: ${response.code}"
                        }
                    }
                }
            })
        }
    }
}
