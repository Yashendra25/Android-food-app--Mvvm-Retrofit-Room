package com.yashendra.foodorderingapp_easyfood.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HeaderViewModel: ViewModel() {
    private val hname = MutableLiveData<String>()
    private val hemail = MutableLiveData<String>()
    val headername: LiveData<String> = hname
    val headeremail: LiveData<String> = hemail

    fun setheadername(text: String) {
        Log.d("normal text",text)
        hname.postValue(text)
        Log.d("postvalue1",hname.value.toString())


    }
    fun setheaderemail(text: String) {
        hemail.postValue(text)


    }
    fun Observename(): LiveData<String> {
        return headername
    }
    fun observemail(): LiveData<String> {
        return headeremail
    }
}