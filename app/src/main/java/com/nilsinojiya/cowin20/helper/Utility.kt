package com.nilsinojiya.cowin20.helper

import android.app.Dialog
import android.app.Notification
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nilsinojiya.cowin20.R
import java.text.SimpleDateFormat
import java.util.*

object Utility {

    var dialog: Dialog? = null
    private var notificationManager: NotificationManagerCompat? = null
    fun displayLoadingWithText(context: Context?, text: String?, cancelable: Boolean) {
        dialog = Dialog(context!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.layout_loading)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(cancelable)
        try {
            dialog!!.show()
        } catch (e: Exception) {
        }
    }

    fun hideLoading() {
        try {
            if (dialog != null) {
                dialog!!.dismiss()
            }
        } catch (e: Exception) {
        }
    }

    fun checkInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        Toast.makeText(context, "No Internet!", Toast.LENGTH_SHORT).show()
        return false
    }

    fun stringToDate(d: String): Date{
        val input = SimpleDateFormat("dd-MM-yyyy")
        return input.parse(d)

    }

    fun dateToString(date: Date?): String{
        val format = SimpleDateFormat("dd-MM-yyy")
        return format.format(date)
    }

    fun incrementDateByOne(date: Date?): Date? {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, 1)
        return calendar.time
    }

    fun decrementDateByOne(date: Date?): Date? {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, -1)
        return calendar.time
    }

    fun createNotification(context: Context, mssg: String) {
        val title: String = "Slots Available"
        val message: String = mssg
        val notification: Notification = NotificationCompat.Builder(context, App.CHANNEL_1_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setOnlyAlertOnce(true)
            .build()
        notificationManager = NotificationManagerCompat.from(context)
        notificationManager!!.notify(1, notification)
    }

}