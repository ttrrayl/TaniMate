package com.example.tanimate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanimate.data.local.UserSession
import com.example.tanimate.data.network.ApiConfig
import kotlinx.coroutines.launch

class ProfileViewModel(private val pref: UserSession): ViewModel() {


    fun logout(){
        viewModelScope.launch {
            pref.logout()
        }
    }
}