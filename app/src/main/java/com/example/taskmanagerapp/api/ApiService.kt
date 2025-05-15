package com.example.taskmanagerapp.api

import com.example.taskmanagerapp.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api/task")
    suspend fun getTasks(): TaskResponse

    @GET("api/category")
    suspend fun getCategories(): CategoryResponse

    @POST("api/task")
    suspend fun createTask(@Body task: Map<String, @JvmSuppressWildcards Any>): Response<Unit>

    @PUT("api/task/{id}")
    suspend fun updateTask(@Path("id") id: Int, @Body task: Map<String, @JvmSuppressWildcards Any>): Response<Unit> // ✅ Safe

    @PUT("api/task/{id}/done")
    suspend fun finishTask(@Path("id") id: Int): Response<Unit>

    @DELETE("api/task/{id}")
    suspend fun deleteTask(@Path("id") id: Int): Response<Unit>
}
