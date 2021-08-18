package com.rj.testebancosolutis.view

import android.content.DialogInterface
import android.content.DialogInterface.*
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rj.testebancosolutis.R
import com.rj.testebancosolutis.adapter.StatementAdapter
import com.rj.testebancosolutis.constants.StatementsConstants
import com.rj.testebancosolutis.databinding.ActivityStatementBinding
import com.rj.testebancosolutis.databinding.CardStatementBinding
import com.rj.testebancosolutis.repository.local.SecurityPreferences
import com.rj.testebancosolutis.viewModel.StatementViewModel


class StatementActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var statementBinding: ActivityStatementBinding
    private lateinit var cardStatementBinding: CardStatementBinding

    private lateinit var mViewModel: StatementViewModel
    private val mAdapter = StatementAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statementBinding = ActivityStatementBinding.inflate(LayoutInflater.from(this))
        cardStatementBinding = CardStatementBinding.inflate(LayoutInflater.from(this))

        val view = statementBinding.root
        setContentView(view)

        mViewModel = ViewModelProvider(this).get(StatementViewModel::class.java)

        loadData()
        loadStatement()

        val recycler = findViewById<RecyclerView>(R.id.recyler_statement)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = mAdapter

        statementBinding.include.imageLogout.setOnClickListener(this)
        statementBinding.swipeRefresh.setOnRefreshListener {
            mAdapter.clear()
            loadStatement()
        }

        observe()

    }

    override fun onBackPressed() {
        this.moveTaskToBack(true)
    }

    private fun loadData() {
        statementBinding.progressStatement.isVisible = true
        val values = mViewModel.loadData()
        statementBinding.include.textName.text = values[StatementsConstants.USER.USER_NAME]
        statementBinding.include.cpfCnpj.text = values[StatementsConstants.USER.USER_CPF]
        statementBinding.include.balance.text = values[StatementsConstants.USER.USER_BALANCE]
        statementBinding.progressStatement.isVisible = false
    }



    override fun onClick(v: View) {
        if (v.id == statementBinding.include.imageLogout.id){
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Saindo...")
            alertDialog.setIcon(R.drawable.ic_baseline_exit_to_app)
            alertDialog.setMessage("Realmente fazer o logout?")
            alertDialog.setPositiveButton("Sim", DialogInterface.OnClickListener { _, _ ->
                deleteCache()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            })
            alertDialog.setNegativeButton("Não", DialogInterface.OnClickListener { _, _ ->})
            val alertShow = alertDialog.show()
            alertShow.getButton(BUTTON_POSITIVE).setTextColor(getColor(R.color.blue))
            alertShow.getButton(BUTTON_NEGATIVE).setTextColor(getColor(R.color.blue))
            alertShow.show()
        }
    }


    private fun loadStatement() {
        mViewModel.loadStatement()
        statementBinding.swipeRefresh.isRefreshing = false
    }


    private fun observe() {
        mViewModel.statement.observe(this, Observer {
            if (it.count() > 0){
                mAdapter.updateStatement(it)
            }
        })

    }

    private fun deleteCache() {
        SecurityPreferences(this).remove(StatementsConstants.USER.USER_NAME)
        SecurityPreferences(this).remove(StatementsConstants.USER.USER_CPF)
        SecurityPreferences(this).remove(StatementsConstants.USER.USER_BALANCE)
        if (SecurityPreferences(this).get(StatementsConstants.FINGERPRINT.USER_FINGERPRINT) != "1"){
            SecurityPreferences(this).remove(StatementsConstants.SHARED.USER_PASSWORD)
        }
    }



}


