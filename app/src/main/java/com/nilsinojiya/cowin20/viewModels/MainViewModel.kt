package com.nilsinojiya.cowin20.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nilsinojiya.cowin20.models.Center
import com.nilsinojiya.cowin20.models.Sessions
import com.nilsinojiya.cowin20.repositorys.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: MainRepository): ViewModel() {
    private val TAG = this::class.java.simpleName

    val center = MutableLiveData<Sessions>()
    val errorMessage = MutableLiveData<String>()

    fun findByPin(pincode: Int, date: String) {
        Log.d(TAG, "findByPin: ")
        val response = repository.findByPin(pincode, date)
        response.enqueue(object : Callback<Sessions> {
            override fun onResponse(call: Call<Sessions>, response: Response<Sessions>) {
                center.postValue(response.body())
                Log.d(TAG, "onResponse: ${response.body()}")
            }
            override fun onFailure(call: Call<Sessions>, t: Throwable) {
                errorMessage.postValue(t.message)
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }
}