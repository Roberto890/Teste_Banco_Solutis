package com.rj.testebancosantander.model

import java.io.Serializable

class UserModel(user: String, password: String) : Serializable {

    var username: String = user
    var password: String = password

}
