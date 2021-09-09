package com.nilsinojiya.cowin20.viewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nilsinojiya.cowin20.models.*
import com.nilsinojiya.cowin20.repositorys.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class MainViewModel(private val repository: MainRepository): ViewModel() {
    private val TAG = this::class.java.simpleName

    val sessions = MutableLiveData<Sessions>()
    val errorMessage = MutableLiveData<String>()
    val states = MutableLiveData<States>()
    val districts = MutableLiveData<Districts>()

    fun findByPin(pincode: Int, date: String) {
        Log.d(TAG, "findByPin: ")
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.findByPin(pincode, date).awaitResponse()
            if(response.isSuccessful){
                sessions.postValue(response.body())
                Log.d(TAG, "onResponse: ${Thread.currentThread().name}")
            } else{
                Log.e(TAG, "onResponse: ${response.errorBody()}")
            }
        }
    }
    
    fun getStates(){
        Log.d(TAG, "getStates: ")
        CoroutineScope(Dispatchers.IO).launch { 
            val response = repository.getStates().awaitResponse()
            if(response.isSuccessful){
                states.postValue(response.body())
                Log.d(TAG, "onResponse: ${Thread.currentThread().name}")
            } else {
                Log.d(TAG, "getStates: ${response.errorBody()}")
            }
        }
    }

    fun getDistricts(state: String){
        Log.d(TAG, "getDistricts: ")
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getDistricts(state).awaitResponse()
            if(response.isSuccessful){
                districts.postValue(response.body())
                Log.d(TAG, "onResponse: ${Thread.currentThread().name}")
            } else {
                Log.d(TAG, "getStates: ${response.errorBody()}")
            }

        }
    }

    fun findByDistrict(districtId: Int, date: String){
        Log.d(TAG, "findByDistrict: ")
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.findByDistrict(districtId, date).awaitResponse()
            if(response.isSuccessful){
                sessions.postValue(response.body())
                Log.d(TAG, "onResponse: ${Thread.currentThread().name}")
            } else{
                Log.e(TAG, "onResponse: ${response.errorBody()}")
            }
        }
    }

}