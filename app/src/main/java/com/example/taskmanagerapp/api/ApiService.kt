package com.example.taskmanagerapp.api

import com.example.taskmanagerapp.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("tasks")
    suspend fun getTasks(): TaskResponse

    @GET("categories")
    suspend fun getCategories(): CategoryResponse

    @POST("tasks")
    suspend fun createTask(@Body task: TaskRequest): Response<Unit>

    @PUT("tasks/{id}")
    suspend fun updateTask(@Path("id") id: Int, @Body task: Map<String, Any>): Response<Unit>

    @PUT("tasks/{id}/done")
    suspend fun finishTask(@Path("id") id: Int): Response<Unit>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Int): Response<Unit>
}
