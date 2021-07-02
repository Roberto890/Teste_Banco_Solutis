package com.rj.testebanco.viewModel

import android.app.Application
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rj.testebanco.constants.StatmentsConstants
import com.rj.testebanco.listener.APIListener
import com.rj.testebanco.model.LoginModel
import com.rj.testebanco.repository.BaseRepository
import com.rj.testebanco.repository.StatmentRepository
import com.rj.testebanco.repository.local.SecurityPreferences
import retrofit2.Call
import java.util.regex.Pattern
import kotlin.math.log

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private val mStatmentRepository = StatmentRepository(application)
    private val mSecurityPreferences = SecurityPreferences(application)

    private val mLogin = MutableLiveData<Int>()
    var login: LiveData<Int> = mLogin

    private val mLastLogin = MutableLiveData<String>()
    var lastLogin: LiveData<String> = mLastLogin

//    private val mValidLogin = MutableLiveData<Boolean>()
//    var validLogin: LiveData<Boolean> = mValidLogin
//
//    private val mValidPassword = MutableLiveData<Int>()
//    var validPassword: LiveData<Int> = mValidPassword

    fun doLogin(user: String, password: String) {

        if(mStatmentRepository.isConnectionAvailable(getApplication())){
            val emailLogin = Patterns.EMAIL_ADDRESS.matcher(user).matches()
            val upperCase = password.contains(Regex("[A-Z]"))
            val specialCharacter = password.contains(Regex("[@#$%^/&+=]"))
            val alphaNumeric = password.contains(Regex("[a-zA-Z0-9]"))
            if (!emailLogin or !specialCharacter or !alphaNumeric){
                mLogin.value = 0

            }

            mStatmentRepository.login(user, password, object : APIListener<LoginModel>{
                override fun onSuccess(model: LoginModel) {
                    mSecurityPreferences.store(StatmentsConstants.USER.USER_NAME, model.name)
                    mSecurityPreferences.store(StatmentsConstants.USER.USER_CPF, model.cpf)
                    mSecurityPreferences.store(StatmentsConstants.USER.USER_BALANCE, model.balance.toString())
                    mSecurityPreferences.store(StatmentsConstants.USER.USER_TOKEN, model.token)
                    mSecurityPreferences.store(StatmentsConstants.SHARED.USER_LOGIN, user)

                    mLogin.value = 1
                }

                override fun onFailure(str: String) {
                    mLogin.value = 0
                }

            })
        }else{
            Toast.makeText(getApplication(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show()
        }
    }

    fun cacheLogin() {
        mLastLogin.value = mSecurityPreferences.get(StatmentsConstants.SHARED.USER_LOGIN)
    }

//    fun isValidEmail(login: String) {
//        mValidLogin.value = Patterns.EMAIL_ADDRESS.matcher(login).matches()
//    }
//
//    fun isValidPassword(password: String) {
//         while (true){
//             val upperCase = password.contains(Regex("[A-Z]"))
//             if (!upperCase){
//                 mValidPassword.value = 1
//                 break
//             }
//
//             val specialCharacter = password.contains(Regex("[@#$%^/&+=]"))
//             if (!specialCharacter){
//                 mValidPassword.value = 2
//                 break
//             }
//
//             val alphaNumeric = password.contains(Regex("[a-zA-Z0-9]"))
//             if (!alphaNumeric){
//                 mValidPassword.value = 3
//                 break
//             }
//
//             break
//         }
//    }

}