package com.example.tanimate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.tanimate.data.local.UserModel
import com.example.tanimate.data.local.UserSession

class HomeViewModel (private val pref: UserSession): ViewModel() {

    fun getToken(): LiveData<UserModel>{
        return pref.getToken().asLiveData()
    }
}