package com.rj.testebanco.repository.remote

import com.rj.testebanco.constants.StatmentsConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitClient {

    companion object {

        private lateinit var retrofit: Retrofit
        private val baseUrl = "https://api.mobile.test.solutis.xyz/"

        private fun getRetrofitInstance(): Retrofit {
            val httpClient = OkHttpClient.Builder()

            if (!Companion::retrofit.isInitialized) {
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient.build())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit
        }

        fun <S> createService(serviceClass: Class<S>): S {
            return getRetrofitInstance().create(serviceClass)

        }
    }

}