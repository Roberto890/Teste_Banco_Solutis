package com.rj.testebancosolutis.viewModel

import android.app.Application
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rj.testebancosolutis.constants.StatementsConstants
import com.rj.testebancosolutis.listener.APIListener
import com.rj.testebancosolutis.model.LoginModel
import com.rj.testebancosolutis.model.UserModel
import com.rj.testebancosolutis.repository.StatementRepository
import com.rj.testebancosolutis.repository.local.SecurityPreferences
import com.rj.testebancosolutis.utils.ChCrypto
import com.rj.testebancosolutis.utils.FingerprintHelper
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private val mStatementRepository = StatementRepository(application)
    private val mSecurityPreferences = SecurityPreferences(application)

    private val mLogin = MutableLiveData<Int>()
    var login: LiveData<Int> = mLogin

    private val mFingerprint = MutableLiveData<Int>()
    var fingerprint: LiveData<Int> = mFingerprint

    private val mLastLogin = MutableLiveData<String>()
    var lastLogin: LiveData<String> = mLastLogin


    fun doLogin(userModel: UserModel) {

        if(mStatementRepository.isConnectionAvailable(getApplication())){
            val emailLogin = Patterns.EMAIL_ADDRESS.matcher(userModel.username).matches()
            val upperCase = userModel.password.contains(Regex("[A-Z]"))
            val specialCharacter = userModel.password.contains(Regex("[@#$%^/&+=]"))
            val alphaNumeric = userModel.password.contains(Regex("[a-zA-Z0-9]"))
            if (!emailLogin or !specialCharacter or !alphaNumeric){
                mLogin.value = 0

            }else{
                mStatementRepository.login(userModel, object : APIListener<LoginModel> {
                    override fun onSuccess(model: LoginModel) {
                        mSecurityPreferences.store(StatementsConstants.USER.USER_NAME, model.name)
                        mSecurityPreferences.store(StatementsConstants.USER.USER_CPF, model.cpf)
                        mSecurityPreferences.store(StatementsConstants.USER.USER_BALANCE, model.balance.toString())
                        mSecurityPreferences.store(StatementsConstants.USER.USER_TOKEN, model.token)
                        mSecurityPreferences.store(StatementsConstants.SHARED.USER_LOGIN, userModel.username)
                        val chars = ('a'..'Z') + ('A'..'Z') + ('0'..'9')
                        val secretKey = List(32) { chars.random() }.joinToString("")
                        mSecurityPreferences.store(StatementsConstants.SHARED.USER_SECRET_KEY, secretKey)
                        val passwordCrypto =  ChCrypto.aesEncrypt(userModel.password, secretKey)
                        mSecurityPreferences.store(StatementsConstants.SHARED.USER_PASSWORD, passwordCrypto)


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
        val fingerPrintVerification = mSecurityPreferences.get(StatementsConstants.FINGERPRINT.USER_FINGERPRINT)
        if (FingerprintHelper.isAuthenticationAvailable(getApplication()) && fingerPrintVerification == "1"){
            mFingerprint.value = fingerPrintVerification.toInt()
        }
    }

}