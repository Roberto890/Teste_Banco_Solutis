package com.rj.testebancosolutis.model

import com.google.gson.annotations.SerializedName
import java.util.*

class StatementModel {

    @SerializedName("data")
    lateinit var data: Date

    @SerializedName("descricao")
    var description: String = ""

    @SerializedName("valor")
    var value: Float = 0.0F
}