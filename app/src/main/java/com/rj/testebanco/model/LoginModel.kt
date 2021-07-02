package com.rj.testebanco.model

import com.google.gson.annotations.SerializedName

class LoginModel {

    @SerializedName("nome")
    var name: String = ""

    @SerializedName("cpf")
    var cpf: String = ""

    @SerializedName("saldo")
    var balance: Float = 0.0F

    @SerializedName("token")
    var token: String = ""
}