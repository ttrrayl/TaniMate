package com.example.tanimate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanimate.data.local.UserSession
import kotlinx.coroutines.launch

class ProfileViewModel(private val pref: UserSession): ViewModel() {

    fun logout(){
        viewModelScope.launch {
            pref.logout()
        }
    }
}