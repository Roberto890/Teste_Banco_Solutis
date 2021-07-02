package com.rj.testebanco.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rj.testebanco.R
import com.rj.testebanco.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        login_password.transformationMethod = PasswordTransformationMethod.getInstance()

        button_login.setOnClickListener(this)

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
                    error_login.visibility = View.VISIBLE
                    login.setTextColor(Color.parseColor("#AAC30000"))
                }
                1 -> {
                    startActivity(Intent(this, StatmentActivity::class.java))
                    finish()
                }
            }
        })
        mViewModel.lastLogin.observe(this, Observer {
            if (it != ""){
                login.setText(it)
            }
        })
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_login){
            handleLogin()
        }
    }

    private fun handleLogin() {
        val user = login.text.toString()
        val password = login_password.text.toString()

        mViewModel.doLogin(user, password)
    }

}
