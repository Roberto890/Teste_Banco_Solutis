package com.rj.testebancosolutis.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rj.testebancosolutis.constants.StatementsConstants
import com.rj.testebancosolutis.listener.APIListener
import com.rj.testebancosolutis.model.StatementModel
import com.rj.testebancosolutis.repository.StatementRepository
import com.rj.testebancosolutis.repository.local.SecurityPreferences

class StatementViewModel(application: Application): AndroidViewModel(application) {

    private val mSecurityPreferences = SecurityPreferences(application)
    private val mStatementRepository = StatementRepository(application)

    private val mStatement = MutableLiveData<List<StatementModel>>()
    var statement: LiveData<List<StatementModel>> = mStatement

    fun loadData(): HashMap<String, String> {

        val name = mSecurityPreferences.get(StatementsConstants.USER.USER_NAME)
        var cpf = mSecurityPreferences.get(StatementsConstants.USER.USER_CPF)
        val balance = mSecurityPreferences.get(StatementsConstants.USER.USER_BALANCE)

        cpf = cpfCnpjMask(cpf)

        val values = HashMap<String,String>()
        values[StatementsConstants.USER.USER_NAME] = name
        values[StatementsConstants.USER.USER_CPF] = cpf.format("")
        values[StatementsConstants.USER.USER_BALANCE] = "%.2f".format(values[StatementsConstants.USER.USER_BALANCE]?.toFloat())
        values[StatementsConstants.USER.USER_BALANCE] = "R$ $balance".replace('.',',')

        return values
    }

    fun loadStatement(){
        mStatementRepository.statement(mSecurityPreferences.get(StatementsConstants.USER.USER_TOKEN), object :
            APIListener<List<StatementModel>> {
            override fun onSuccess(model: List<StatementModel>) {
                mStatement.value = model
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