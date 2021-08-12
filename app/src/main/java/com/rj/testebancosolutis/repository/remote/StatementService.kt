package com.rj.testebancosolutis.repository.remote


import com.rj.testebancosolutis.model.LoginModel
import com.rj.testebancosolutis.model.StatementModel
import retrofit2.Call
import retrofit2.http.*

interface StatementService {

    @POST("login")
    fun login(@Body body: HashMap<String, String>): Call<LoginModel>

    @GET("extrato")
    fun statement(@Header("token") token: String): Call<List<StatementModel>>

}