package com.example.tanimate.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanimate.data.local.UserModel
import com.example.tanimate.data.local.UserSession
import com.example.tanimate.data.network.ApiConfig
import com.example.tanimate.data.network.responses.ErrorResponse
import com.example.tanimate.data.network.responses.RegisterResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val pref: UserSession) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun register( email: String, name: String, password: String, konfirpassword: String) {
        _isLoading.value = true
        val param = JsonObject().apply {
            addProperty("email", email)
            addProperty("username", name)
            addProperty("password", password)
            addProperty("confirmPassword", konfirpassword)
        }
        val client = ApiConfig.getApiService().register(param)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responBody = response.body()
                    if (responBody != null){
                        _msg.value = "Daftar Berhasil"
                        viewModelScope.launch {
                            pref.saveToken(
                                UserModel(false,"")
                            )
                        }
                        _isError.value = false
                    } else {
                        _isError.value = true
                    }
                } else {
                    val errorResponse = Gson().fromJson(response.errorBody()!!.charStream(), ErrorResponse::class.java)


                    _msg.value = errorResponse.message!!
                    _isError.value = true
                    Log.e(ContentValues.TAG, "onFailure: ${errorResponse.message}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _msg.value = t.message.toString()
                _isError.value = true
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}