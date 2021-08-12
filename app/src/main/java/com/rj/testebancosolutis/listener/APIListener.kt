package com.rj.testebancosolutis.listener

interface APIListener<T> {

    fun onSuccess(model: T)

    fun onFailure(str: String)

}