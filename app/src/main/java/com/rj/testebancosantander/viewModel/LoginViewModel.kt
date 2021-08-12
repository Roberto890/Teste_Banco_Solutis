package com.rj.testebancosantander.viewModel

import android.app.Application
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rj.testebancosantander.constants.StatementsConstants
import com.rj.testebancosantander.listener.APIListener
import com.rj.testebancosantander.model.LoginModel
import com.rj.testebancosantander.model.UserModel
import com.rj.testebancosantander.repository.StatementRepository
import com.rj.testebancosantander.repository.local.SecurityPreferences

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private val mStatementRepository = StatementRepository(application)
    private val mSecurityPreferences = SecurityPreferences(application)

    private val mLogin = MutableLiveData<Int>()
    var login: LiveData<Int> = mLogin

    private val mLastLogin = MutableLiveData<String>()
    var lastLogin: LiveData<String> = mLastLogin


    fun doLogin(UserModel: UserModel) {

        if(mStatementRepository.isConnectionAvailable(getApplication())){
            val emailLogin = Patterns.EMAIL_ADDRESS.matcher(UserModel.username).matches()
            val upperCase = UserModel.password.contains(Regex("[A-Z]"))
            val specialCharacter = UserModel.password.contains(Regex("[@#$%^/&+=]"))
            val alphaNumeric = UserModel.password.contains(Regex("[a-zA-Z0-9]"))
            if (!emailLogin or !specialCharacter or !alphaNumeric){
                mLogin.value = 0

            }else{
                mStatementRepository.login(UserModel, object : APIListener<LoginModel>{
                    override fun onSuccess(model: LoginModel) {
                        mSecurityPreferences.store(StatementsConstants.USER.USER_NAME, model.name)
                        mSecurityPreferences.store(StatementsConstants.USER.USER_CPF, model.cpf)
                        mSecurityPreferences.store(StatementsConstants.USER.USER_BALANCE, model.balance.toString())
                        mSecurityPreferences.store(StatementsConstants.USER.USER_TOKEN, model.token)
                        mSecurityPreferences.store(StatementsConstants.SHARED.USER_LOGIN, UserModel.username)

                        mLogin.value = 1
                    }

                    override fun onFailure(str: String) {
                        mLogin.value = 0
                    }

                })
            }

        }else{
            Toast.makeText(getApplication(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show()
        }
    }

    fun cacheLogin() {
        mLastLogin.value = mSecurityPreferences.get(StatementsConstants.SHARED.USER_LOGIN)
    }

}