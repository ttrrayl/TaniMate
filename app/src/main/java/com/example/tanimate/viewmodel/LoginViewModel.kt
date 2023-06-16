package com.example.tanimate.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.tanimate.data.local.UserModel
import com.example.tanimate.data.local.UserSession
import com.example.tanimate.data.network.ApiConfig
import com.example.tanimate.data.network.responses.LoginResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserSession): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin

    fun getToken(): LiveData<UserModel> {
        return pref.getToken().asLiveData()
    }

    fun authenticate(name: String, password: String) {
        _isLoading.value = true
        val param = JsonObject().apply {
            addProperty("username", name)
            addProperty("password", password)
        }
        val client = ApiConfig.getApiService().login(param)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responBody = response.body()
                    if (responBody != null){
                        _isLogin.value = true
                        viewModelScope.launch {
                            if (getToken().value == null) pref.saveToken(
                                UserModel(
                                    name,
                                    true,
                                    responBody.accessToken
                                )
                            )
                            pref.login(responBody.accessToken)
                        }
                    } else {
                        _msg.value = responBody?.message
                        _isLogin.value = false
                    }
                } else {
                    val responBody = Gson().fromJson(
                        response.errorBody()?.charStream(), LoginResponse::class.java
                    )
                    _msg.value = responBody.message
                    _isLogin.value = false
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _isLogin.value = false
                Log.e("LoginViewModel","onFailure: ${t.message}")
            }
        })
    }
}