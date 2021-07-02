package com.rj.testebanco.repository

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.rj.testebanco.constants.StatmentsConstants
import com.rj.testebanco.listener.APIListener
import com.rj.testebanco.model.LoginModel
import com.rj.testebanco.model.StatmentModel
import com.rj.testebanco.repository.remote.RetrofitClient
import com.rj.testebanco.repository.remote.StatmentService
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StatmentRepository(val context: Context) : BaseRepository(context) {

    private val mRemote = RetrofitClient.createService(StatmentService::class.java)

    fun login(username: String, password: String, param: APIListener<LoginModel>){

        val login = HashMap<String, String>()
        login["username"] = username
        login["password"] = password
        val call: Call<LoginModel> = mRemote.login(login)

        call.enqueue(object : Callback<LoginModel> {
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>
            ) {
                if (response.code() != StatmentsConstants.HTTP.SUCCESS) {
                    val response = JSONObject(response.errorBody()!!.string()).get("message")
                    param.onFailure(response.toString())
                } else {
                    response.body()?.let { param.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                Toast.makeText(context, "Falha no login", Toast.LENGTH_SHORT).show()
            }

        })

    }

    fun statment(token:String, param: APIListener<List<StatmentModel>>){
        val call: Call<List<StatmentModel>> = mRemote.statment(token)

        call.enqueue(object : Callback<List<StatmentModel>>{
            override fun onResponse(call: Call<List<StatmentModel>>, response: Response<List<StatmentModel>>
            ) {
                if (response.code() != StatmentsConstants.HTTP.SUCCESS) {
                    val validation = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    Toast.makeText(context, "Falha ao listar extrato", Toast.LENGTH_SHORT).show()
                } else {
                    response.body()?.let { param.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<List<StatmentModel>>, t: Throwable) {
                Toast.makeText(context, "Falha ao listar extrato", Toast.LENGTH_SHORT).show()
            }

        })
    }

}



