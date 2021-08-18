package com.rj.testebancosolutis.view

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rj.testebancosolutis.R
import com.rj.testebancosolutis.constants.StatementsConstants
import com.rj.testebancosolutis.databinding.ActivityLoginBinding
import com.rj.testebancosolutis.model.UserModel
import com.rj.testebancosolutis.repository.local.SecurityPreferences
import com.rj.testebancosolutis.utils.ChCrypto
import com.rj.testebancosolutis.utils.FingerprintHelper
import com.rj.testebancosolutis.viewModel.LoginViewModel
import java.util.concurrent.Executor


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var mViewModel: LoginViewModel
    private lateinit var mSecurityPreferences: SecurityPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        val view = binding.root
        setContentView(view)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.loginPassword.transformationMethod = PasswordTransformationMethod.getInstance()

        binding.buttonLogin.setOnClickListener(this)

        mSecurityPreferences = SecurityPreferences(this)

        cacheLogin()

        observe()

    }

    private fun cacheLogin() {
        mViewModel.cacheLogin()
    }

    private fun observe(){

        mViewModel.login.observe(this, Observer {
            when (it) {
                0 -> {
                    binding.errorLogin.visibility = View.VISIBLE
                    binding.progressLogin.isVisible = false
                    binding.buttonLogin.isEnabled = true
                }
                1 -> {
                    binding.progressLogin.isVisible = false
                    binding.buttonLogin.isEnabled = true
                    startActivity(Intent(this, StatementActivity::class.java))
                    finish()
                }
            }
        })

        mViewModel.lastLogin.observe(this, Observer {
            if (it != ""){
                binding.login.setText(it)
            }
        })

        mViewModel.fingerprint.observe(this, Observer {
            if (it == 1){
                showAuthentication()
            }
        })
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_login){
            handleLogin()
        }
    }

    private fun handleLogin() {
        val user = binding.login.text.toString()
        val password = binding.loginPassword.text.toString()
        var userModel = UserModel(user,password)
        binding.progressLogin.isVisible = true
        binding.buttonLogin.isEnabled = false
        mViewModel.doLogin(userModel)
    }

    private fun showAuthentication() {
        val executor: Executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this@LoginActivity, executor, object : BiometricPrompt.AuthenticationCallback(){

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                val user = mSecurityPreferences.get(StatementsConstants.SHARED.USER_LOGIN)
                val userPassword = mSecurityPreferences.get(StatementsConstants.SHARED.USER_PASSWORD)
                val userSecretKey = mSecurityPreferences.get(StatementsConstants.SHARED.USER_SECRET_KEY)
                val password = ChCrypto.aesDecrypt(userPassword, userSecretKey)
                val userModel = UserModel(user,password)
                binding.progressLogin.isVisible = true
                binding.buttonLogin.isEnabled = false
                mViewModel.doLogin(userModel)

            }

        })
        val info: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login com digital")
            .setDescription("Toque no sensor para acessar sua conta")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(info)
    }

}
