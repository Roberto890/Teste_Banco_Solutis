package com.rj.testebanco.model

import com.google.gson.annotations.SerializedName
import java.util.*

class StatmentModel {

    @SerializedName("data")
    lateinit var data: Date

    @SerializedName("descricao")
    var description: String = ""

    @SerializedName("valor")
    var value: Float = 0.0F
}