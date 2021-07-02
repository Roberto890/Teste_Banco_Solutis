package com.rj.testebanco.repository.remote


import com.rj.testebanco.model.LoginModel
import com.rj.testebanco.model.StatmentModel
import retrofit2.Call
import retrofit2.http.*

interface StatmentService {

    @POST("login")
    fun login(@Body body: HashMap<String, String>): Call<LoginModel>

    @GET("extrato")
    fun statment(@Header("token") token: String): Call<List<StatmentModel>>

}