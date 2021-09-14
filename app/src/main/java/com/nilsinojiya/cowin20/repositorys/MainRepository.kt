package com.nilsinojiya.cowin20.repositorys

import com.nilsinojiya.cowin20.models.Districts
import com.nilsinojiya.cowin20.models.Sessions
import com.nilsinojiya.cowin20.models.States
import com.nilsinojiya.cowin20.services.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call

class MainRepository(private val retrofitService: RetrofitService) {
    suspend fun findByPin(pincode: Int, date: String) : Flow<Call<Sessions>> = flow {
        emit(retrofitService.findByPin(pincode, date))
    }.flowOn(Dispatchers.IO)

    suspend fun getStates() : Flow<Call<States>> = flow {
        emit(retrofitService.getStates())
    }.flowOn(Dispatchers.IO)

    suspend fun getDistricts(state: String) : Flow<Call<Districts>> = flow {
        emit(retrofitService.getDistricts(state))
    }.flowOn(Dispatchers.IO)

    suspend fun findByDistrict(districtId: Int, date: String) : Flow<Call<Sessions>> = flow {
        emit(retrofitService.findByDistrict(districtId, date))
    }.flowOn(Dispatchers.IO)
}