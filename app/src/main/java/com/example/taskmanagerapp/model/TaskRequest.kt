package com.example.taskmanagerapp.model

data class TaskRequest(
    val title: String,
    val description: String?,
    val category_id: Int
)
