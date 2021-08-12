package com.rj.testebancosolutis.repository

import android.content.Context
import android.widget.Toast
import com.rj.testebancosolutis.constants.StatementsConstants
import com.rj.testebancosolutis.listener.APIListener
import com.rj.testebancosolutis.model.LoginModel
import com.rj.testebancosolutis.model.StatementModel
import com.rj.testebancosolutis.model.UserModel
import com.rj.testebancosolutis.repository.remote.RetrofitClient
import com.rj.testebancosolutis.repository.remote.StatementService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StatementRepository(val context: Context) : BaseRepository(context) {

    private val mRemote = RetrofitClient.createService(StatementService::class.java)

    fun login(userModel: UserModel, param: APIListener<LoginModel>){

        val login = HashMap<String, String>()
        login["username"] = userModel.username
        login["password"] = userModel.password
        val call: Call<LoginModel> = mRemote.login(login)

        call.enqueue(object : Callback<LoginModel> {
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>
            ) {
                if (response.code() != StatementsConstants.HTTP.SUCCESS) {
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

    fun statement(token:String, param: APIListener<List<StatementModel>>){
        val call: Call<List<StatementModel>> = mRemote.statement(token)

        call.enqueue(object : Callback<List<StatementModel>>{
            override fun onResponse(call: Call<List<StatementModel>>, response: Response<List<StatementModel>>
            ) {
                if (response.code() != StatementsConstants.HTTP.SUCCESS) {
//                    val validation = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    Toast.makeText(context, "Falha ao listar extrato", Toast.LENGTH_SHORT).show()
                } else {
                    response.body()?.let { param.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<List<StatementModel>>, t: Throwable) {
                Toast.makeText(context, "Falha ao listar extrato", Toast.LENGTH_SHORT).show()
            }

        })
    }

}



