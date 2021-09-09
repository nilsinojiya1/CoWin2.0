package com.nilsinojiya.cowin20.services

import com.nilsinojiya.cowin20.constants.AppConstants
import com.nilsinojiya.cowin20.models.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.logging.Level

interface RetrofitService {

    @GET("appointment/sessions/public/findByPin")
    fun findByPin(
        @Query("pincode") pincode: Int,
        @Query("date") data: String
    ): Call<Sessions>

    @GET("admin/location/states")
    fun getStates(): Call<States>

    @GET("admin/location/districts/{state}")
    fun getDistricts(@Path("state") state: String): Call<Districts>

    @GET("appointment/sessions/public/findByDistrict")
    fun findByDistrict(
        @Query("district_id") districtId: Int,
        @Query("date") date: String
    ): Call<Sessions>

    companion object {
        var retrofitService: RetrofitService? = null
        private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        private val okHttp = OkHttpClient.Builder().addInterceptor(logging)
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttp.build())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }

}