package com.example.taskmanagerapp.model

data class Category(
    val id: Int,
    val name: String
)

data class Task(
    val id: Int,
    val title: String,
    val category: Category,
    val description: String?,
    var done: Boolean,
    val updated_at: String
)

