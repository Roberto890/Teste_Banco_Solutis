package com.rj.testebancosantander.listener

interface APIListener<T> {

    fun onSuccess(model: T)

    fun onFailure(str: String)

}