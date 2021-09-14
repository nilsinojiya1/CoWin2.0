package com.nilsinojiya.cowin20.viewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.Api
import com.nilsinojiya.cowin20.helper.ApiState
import com.nilsinojiya.cowin20.models.*
import com.nilsinojiya.cowin20.repositorys.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class MainViewModel(private val repository: MainRepository): ViewModel() {
    private val TAG = this::class.java.simpleName

    private val sessionsStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)
    val _sessionsStateFlow: StateFlow<ApiState> = sessionsStateFlow

    private val statesStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)
    val _statesStateFlow: StateFlow<ApiState> = statesStateFlow

    private val districtsStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)
    val _districtsStateFlow: StateFlow<ApiState> = districtsStateFlow

    val sessions = MutableLiveData<Sessions>()
    val states = MutableLiveData<States>()
    val districts = MutableLiveData<Districts>()

    fun findByPin(pincode: Int, date: String) {
        Log.d(TAG, "findByPin: ")
        sessionsStateFlow.value = ApiState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            repository.findByPin(pincode, date)
                .catch {e ->
                    sessionsStateFlow.value = ApiState.Failure(e)
                }
                .collect {
                    val response = it.awaitResponse().body()!!
                    if(response.centers.isEmpty()){
                       sessionsStateFlow.value = ApiState.Empty
                    } else {
                       sessionsStateFlow.value = ApiState.SuccessSession(response)
                    }
                }


        }
    }
    
    fun getStates(){
        Log.d(TAG, "getStates: ")
        statesStateFlow.value = ApiState.Loading
        CoroutineScope(Dispatchers.IO).launch { 
            repository.getStates()
                .catch { e ->
                    statesStateFlow.value = ApiState.Failure(e)
                }
                .collect {
                    val response = it.awaitResponse().body()!!
                    if(response.states.isEmpty()){
                        statesStateFlow.value = ApiState.Empty
                    } else {
                        statesStateFlow.value = ApiState.SuccessStates(response)
                    }
                    Log.d(TAG, "onResponse: ${Thread.currentThread().name}")
                }
        }
    }

    fun getDistricts(state: String){
        Log.d(TAG, "getDistricts: ")
        districtsStateFlow.value = ApiState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            repository.getDistricts(state)
                .catch { e ->
                    districtsStateFlow.value = ApiState.Failure(e)
                }
                .collect {
                    val response = it.awaitResponse().body()!!
                    if(response.districts.isEmpty()){
                        districtsStateFlow.value = ApiState.Empty
                    } else {
                        districtsStateFlow.value = ApiState.SuccessDistricts(response)
                    }
                    Log.d(TAG, "onResponse: ${Thread.currentThread().name}")
                }
        }
    }

    fun findByDistrict(districtId: Int, date: String){
        Log.d(TAG, "findByDistrict: ")
        sessionsStateFlow.value = ApiState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            repository.findByDistrict(districtId, date)
                .catch {e ->
                    sessionsStateFlow.value = ApiState.Failure(e)
                }
                .collect {
                    val response = it.awaitResponse().body()!!
                    if(response.centers.isEmpty()){
                        sessionsStateFlow.value = ApiState.Empty
                    } else {
                        sessionsStateFlow.value = ApiState.SuccessSession(response)
                    }
                }
        }
    }

}