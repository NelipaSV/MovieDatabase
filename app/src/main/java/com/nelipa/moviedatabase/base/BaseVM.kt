package com.nelipa.moviedatabase.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nelipa.moviedatabase.models.ResponseError
import com.nelipa.moviedatabase.network.ResponseHandler
import com.nelipa.moviedatabase.utility.Event

abstract class BaseVM: ViewModel() {

    val responseHandler = ResponseHandler()
    private var error = MutableLiveData<Event<String?>>()

    fun getLoadError(): LiveData<Event<String?>> = error
    fun handleResponseError(error: ResponseError?) {
        this.error.postValue(Event(error?.message))
    }
}