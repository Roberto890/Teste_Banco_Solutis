package com.rj.testebancosolutis.view

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rj.testebancosolutis.R
import com.rj.testebancosolutis.databinding.ActivityLoginBinding
import com.rj.testebancosolutis.model.UserModel
import com.rj.testebancosolutis.viewModel.LoginViewModel


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        val view = binding.root
        setContentView(view)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.loginPassword.transformationMethod = PasswordTransformationMethod.getInstance()

        binding.buttonLogin.setOnClickListener(this)

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

    private fun loginVerify(code: Int){
        when (code) {
            0 -> {
                binding.errorLogin.visibility = View.VISIBLE
            }
            1 -> {
                startActivity(Intent(this, StatementActivity::class.java))
                finish()
            }
        }
    }

}
