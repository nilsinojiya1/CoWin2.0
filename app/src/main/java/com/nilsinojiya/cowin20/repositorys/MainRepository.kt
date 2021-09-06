package com.nilsinojiya.cowin20.repositorys

import com.nilsinojiya.cowin20.services.RetrofitService

class MainRepository(private val retrofitService: RetrofitService) {
    fun findByPin(pincode: Int, date: String) = retrofitService.findByPin(pincode, date)
}