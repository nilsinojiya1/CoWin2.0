package com.nilsinojiya.cowin20.helper

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.nilsinojiya.cowin20.models.Sessions
import com.nilsinojiya.cowin20.repositorys.MainRepository
import com.nilsinojiya.cowin20.services.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.util.*

class SlotCheckWorker(context: Context, params: WorkerParameters): Worker(context, params) {

    private val TAG = this::class.java.simpleName
    private val retrofitService = RetrofitService.getInstance()
    private val repository: MainRepository = MainRepository(retrofitService)
    private lateinit var sessions: Sessions
    private var pref = SharedPreferencesHelper
    override fun doWork(): Result {
        try {
            pref.init(applicationContext)
            if(Utility.checkInternet(applicationContext)){
                findByPin(pref.getPin())
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    private fun findByPin(pincode: Int) {
        Log.d(
            TAG,
            "findByPin: ${Utility.dateToString(Utility.incrementDateByOne(Date()))} $pincode"
        )
        CoroutineScope(Dispatchers.IO).launch {
            val response =
                repository.findByPin(pincode, Utility.dateToString(Utility.incrementDateByOne(Date()))).awaitResponse()
            if (response.isSuccessful) {
                sessions = response.body()!!
                //Log.d(TAG, "findByPin: ${sessions.centers.size}")
                for (center in sessions.centers) {
                    if (pref.getVaccine()?.toUpperCase().equals(center.vaccine.toUpperCase())) {
                        if (pref.getDose() == 1 && center.availableCapacityDose1 > 0) {
                            Utility.createNotification(
                                applicationContext,
                                "${center.availableCapacityDose1} Slots for ${pref.getVaccine()} Dose: 1 available at ${center.name}"
                            )
                            //Log.d(TAG, "findByPin: ${center.availableCapacityDose1} Slots for ${pref.VACCINE} Dose: 1 available at ${center.name}")
                        } else if (pref.getDose() == 2 && center.availableCapacityDose2 > 0) {
                            Utility.createNotification(
                                applicationContext,
                                "${center.availableCapacityDose2} Slots for ${pref.getVaccine()} Dose: 2 available at ${center.name}"
                            )
                            //Log.d(TAG, "findByPin: ${center.availableCapacityDose2} Slots for ${pref.getVaccine()} Dose: 2 available at ${center.name}")
                        }
                    }
                }
                Log.d(TAG, "onResponse: ${Thread.currentThread().name}")
            } else {
                Log.e(TAG, "onResponse: ${response.errorBody()}")
            }
        }
    }
}