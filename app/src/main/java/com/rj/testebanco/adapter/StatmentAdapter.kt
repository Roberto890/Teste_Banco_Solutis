package com.rj.testebanco.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.rj.testebanco.R
import com.rj.testebanco.model.StatmentModel
import kotlinx.android.synthetic.main.card_statment.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class StatmentAdapter(): RecyclerView.Adapter<StatmentAdapter.StatmentHolder>() {

    private var mStatment: List<StatmentModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatmentHolder {
        val statmentView = LayoutInflater.from(parent.context).inflate(R.layout.card_statment, parent, false)

        return StatmentHolder(statmentView)
    }

    override fun onBindViewHolder(holder: StatmentHolder, position: Int) {
        holder.bindingData(mStatment[position])
    }

    override fun getItemCount(): Int {
        return mStatment.count()
    }

    fun updateStatment(list: List<StatmentModel>){
        mStatment = list
        notifyDataSetChanged()
    }

    inner class StatmentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindingData(statment: StatmentModel) {
            val formatter = SimpleDateFormat("dd/MM/yyyy")

            itemView.card_description.text = statment.description
            itemView.card_date.text = formatter.format(statment.data)
            itemView.card_value.text = formatValue(statment.value)
        }

        private fun formatValue(value: Float): String {
            return if (value > 0 ) {
                "R$$value".replace(".",",")
            }else {
                itemView.card_value.setTextColor(Color.parseColor("#F60404"))
                val strValue = value.toString()
                strValue[0] + "R$" + strValue.substring(1, strValue.lastIndex+1).replace(".",",")
            }

        }

    }



}