package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.response.RegisterResponse
import com.example.myapplication.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationViewModel : ViewModel() {

    private val _registrationInfo = MutableLiveData<RegisterResponse>()
    val registrationInfo: LiveData<RegisterResponse> = _registrationInfo

    fun postRegister(email: String, name:String, password:String) {
        val client = ApiConfig.getApiService().register(name,email,password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    _registrationInfo.value = response.body()
                } else {
                    Log.e("PostRegistrasi", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("PostRegistrasi", "onFailure: ${t.message.toString()}")
            }
        })
    }


}
