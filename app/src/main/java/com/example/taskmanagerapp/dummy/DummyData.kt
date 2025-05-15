package com.example.taskmanagerapp.dummy

import com.example.taskmanagerapp.model.Category
import com.example.taskmanagerapp.model.Task

object DummyData {
    val important = Category(1, "Important")
    val urgent = Category(2, "Urgent")
    val regular = Category(3, "Regular")

    val tasks = listOf(
        Task(1, "Pay bills", important, "Electricity and water", false, "2025-05-15 10:00"),
        Task(2, "Doctor appointment", urgent, "Fixing my right leg", false, "2025-05-14 09:00"),
        Task(3, "Grocery shopping", regular, "Buy vegetables and fruits", true, "2025-05-13 18:30"),
        Task(4, "Project report", important, "Finish the draft report", false, "2025-05-12 16:00"),
        Task(5, "Car service", urgent, "Fixing Front Bumper", true, "2025-05-11 14:00")
    )
}
