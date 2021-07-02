package com.rj.testebanco.constants

import com.google.gson.annotations.SerializedName

class StatmentsConstants private constructor() {

    // SharedPreferences
    object SHARED {
        const val USER_LOGIN = "user_login"
    }

    object HTTP {
        const val SUCCESS = 200
    }

    object USER{
        const val USER_NAME = "user_name"

        const val USER_CPF = "user_cpf"

        const val USER_BALANCE = "user_balance"

        const val USER_TOKEN = "user_token"
    }
}