package com.nilsinojiya.cowin20.helper

import android.app.Application
import android.app.Notification
import android.app.NotificationManager

import android.app.NotificationChannel

import android.os.Build




class App: Application() {


    companion object{
        val CHANNEL_1_ID = "channelSlot"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                CHANNEL_1_ID,
                "Slots Available",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "This is Channel for slots availability notification"
            channel1.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel1)
        }
    }

}