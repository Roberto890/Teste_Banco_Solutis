package com.rj.testebancosolutis.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rj.testebancosolutis.databinding.CardStatementBinding
import com.rj.testebancosolutis.model.StatementModel
import java.text.SimpleDateFormat
import java.util.*


class StatementAdapter: RecyclerView.Adapter<StatementAdapter.StatementHolder>() {

    private var mStatement: List<StatementModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatementHolder {
        val statementView = CardStatementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatementHolder(statementView)
    }

    override fun onBindViewHolder(holder: StatementHolder, position: Int) {
        holder.bindingData(mStatement[position])
    }

    override fun getItemCount(): Int {
        return mStatement.count()
    }

    fun clear() {
        mStatement = emptyList()
        notifyDataSetChanged()
    }

    fun updateStatement(list: List<StatementModel>){
        mStatement = list
        notifyDataSetChanged()
    }

    inner class StatementHolder(private val binding: CardStatementBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindingData(statement: StatementModel) {
            val formatter = SimpleDateFormat("dd/MM/yyyy")

            binding.cardDescription.text = statement.description
            binding.cardDate.text = formatter.format(statement.data)
            binding.cardValue.text = formatValue(statement.value)
        }

        private fun formatValue(value: Float): String {
            return if (value > 0 ) {
                binding.cardPayment.text = "Recebimento"
                val value = "%.2f".format(value)
                "R$ $value".replace(".",",")
            }else {
                binding.cardValue.setTextColor(Color.parseColor("#F60404"))
                val strValue = "%.2f".format(value)
                strValue[0] + "R$ " + strValue.substring(1, strValue.lastIndex+1).replace(".",",")
            }

        }

    }


}