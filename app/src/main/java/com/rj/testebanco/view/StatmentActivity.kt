package com.rj.testebanco.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rj.testebanco.R
import com.rj.testebanco.Supplier
import com.rj.testebanco.adapter.StatmentAdapter
import com.rj.testebanco.constants.StatmentsConstants
import com.rj.testebanco.repository.StatmentRepository
import com.rj.testebanco.repository.local.SecurityPreferences
import com.rj.testebanco.viewModel.StatmentViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.header_statment.*
import kotlinx.android.synthetic.main.header_statment.view.*


class StatmentActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mViewModel: StatmentViewModel
    private val mAdapter = StatmentAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statment)

        mViewModel = ViewModelProvider(this).get(StatmentViewModel::class.java)

        loadData()
        loadStatment()

        val recycler = findViewById<RecyclerView>(R.id.recyler_statment)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = mAdapter

        image_logout.setOnClickListener(this)

        observe()

    }

    private fun loadData() {
        val values = mViewModel.loadData()
        text_name.text = values[StatmentsConstants.USER.USER_NAME]
        cpf_cnpj.text = values[StatmentsConstants.USER.USER_CPF]
        balance.text = values[StatmentsConstants.USER.USER_BALANCE]
    }

    override fun onClick(v: View) {
        if (v.id == R.id.image_logout){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadStatment() {
        mViewModel.loadStatment()
    }


    private fun observe() {
        mViewModel.statment.observe(this, Observer {
            if (it.count() > 0){
                mAdapter.updateStatment(it)
            }
        })

    }

}