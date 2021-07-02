package com.rj.testebanco.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rj.testebanco.constants.StatmentsConstants
import com.rj.testebanco.listener.APIListener
import com.rj.testebanco.model.StatmentModel
import com.rj.testebanco.repository.StatmentRepository
import com.rj.testebanco.repository.local.SecurityPreferences

class StatmentViewModel(application: Application): AndroidViewModel(application) {

    private val mSecurityPreferences = SecurityPreferences(application)
    private val mStatmentRepository = StatmentRepository(application)

    private val mStatment = MutableLiveData<List<StatmentModel>>()
    var statment: LiveData<List<StatmentModel>> = mStatment

    fun loadData(): HashMap<String, String> {

        val name = mSecurityPreferences.get(StatmentsConstants.USER.USER_NAME)
        var cpf = mSecurityPreferences.get(StatmentsConstants.USER.USER_CPF)
        val balance = mSecurityPreferences.get(StatmentsConstants.USER.USER_BALANCE)

        cpf = cpfCnpjMask(cpf)

        val values = HashMap<String,String>()
        values[StatmentsConstants.USER.USER_NAME] = name
        values[StatmentsConstants.USER.USER_CPF] = cpf.format("")
        values[StatmentsConstants.USER.USER_BALANCE] = balance.replace('.',',')

        return values
    }

    fun loadStatment(){
        mStatmentRepository.statment(mSecurityPreferences.get(StatmentsConstants.USER.USER_TOKEN), object : APIListener<List<StatmentModel>>{
            override fun onSuccess(model: List<StatmentModel>) {
                mStatment.value = model
            }

            override fun onFailure(str: String) {
                Toast.makeText(getApplication(),  "Extrato não foi atualizado", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun cpfCnpjMask(cpf: String): String {
        var maskCpf = ""
        var mask = ""

        if(cpf.count() == 11) mask = "###.###.###-##"
        else if(cpf.count() == 14) mask = "##.###.###/####-##"

        var i = 0
        for (m in mask.toCharArray()) {
            if (m != '#') {
                maskCpf += m
                continue
            }
            try {
                maskCpf += cpf[i]
            } catch (e: Exception) {
                break
            }
            i++
        }
        return maskCpf
    }


}