package com.rj.testebanco.listener

interface APIListener<T> {

    fun onSuccess(model: T)

    fun onFailure(str: String)

}