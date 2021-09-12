package com.nilsinojiya.cowin20.helper

import android.content.Context
import android.content.SharedPreferences
import kotlin.math.PI

object SharedPreferencesHelper {

    private lateinit var prefs: SharedPreferences

    private const val PREFS_NAME = "params"

    const val IS_NOTIFICATION_ON = "IS_NOTIFICATION_ON"
    const val DOSE = "DOSE"
    const val VACCINE = "VACCINE"
    const val PIN = "PIN"

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getIsNotificationOn(): Boolean{
        return prefs.getBoolean(IS_NOTIFICATION_ON,true)
    }

    fun setIsNotificationOn(bool: Boolean){
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor){
            putBoolean(IS_NOTIFICATION_ON, bool)
            commit()
        }
    }

    fun setDose(dose: Int){
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor){
            putInt(DOSE, dose)
            commit()
        }
    }

    fun getDose(): Int{
        return prefs.getInt(DOSE,1)
    }
    fun setPin(pin: Int){
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor){
            putInt(PIN, pin)
            commit()
        }
    }

    fun getPin(): Int{
        return prefs.getInt(PIN,388001)
    }

    fun setVaccine(vaccine: String){
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor){
            putString(VACCINE, vaccine)
            commit()
        }
    }

    fun getVaccine(): String?{
        return prefs.getString(VACCINE, "COVISHIELD")
    }
}