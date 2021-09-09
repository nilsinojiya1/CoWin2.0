package com.nilsinojiya.cowin20.repositorys

import com.nilsinojiya.cowin20.services.RetrofitService

class MainRepository(private val retrofitService: RetrofitService) {
    suspend fun findByPin(pincode: Int, date: String) = retrofitService.findByPin(pincode, date)
    suspend fun getStates() = retrofitService.getStates()
    suspend fun getDistricts(state: String) = retrofitService.getDistricts(state)
    suspend fun findByDistrict(districtId: Int, date: String) = retrofitService.findByDistrict(districtId, date)
}