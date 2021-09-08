package com.nilsinojiya.cowin20.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nilsinojiya.cowin20.databinding.ItemCenterBinding
import com.nilsinojiya.cowin20.models.Center
import com.nilsinojiya.cowin20.models.Sessions
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CenterAdapter: RecyclerView.Adapter<MainViewHolder>() {
    private val TAG = this::class.java.simpleName

    var centers = mutableListOf<Center>()

    fun setCenterList(sessions: Sessions) {
        this.centers = sessions.centers.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCenterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val center = centers[position]
        holder.binding.tvName.text = center.name
        holder.binding.tvFirstDose.text = center.availableCapacityDose1.toString()
        holder.binding.tvSecondDose.text = center.availableCapacityDose2.toString()
        if(center.feeType == "Free"){
            holder.binding.tvCost.text = "Free"
        } else {
            holder.binding.tvCost.text = center.fee + " RS."
        }
        if(center.allowAllAge){
            holder.binding.tvAge.text = "18+"
        } else {
            holder.binding.tvAge.text = "${center.minAgeLimit} - ${center.maxAgeLimit}"
        }
        try {
            val inFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
            val date: Date = inFormat.parse(center.from)
            val outFormat = SimpleDateFormat("hh a", Locale.US)
            holder.binding.tvTime.text = outFormat.format(date).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
            holder.binding.tvTime.text = "9 AM"
        }
        //Log.d(TAG, "onBindViewHolder: ${center.name}")
    }

    override fun getItemCount(): Int {
        return centers.size
    }
}

class MainViewHolder(val binding: ItemCenterBinding) : RecyclerView.ViewHolder(binding.root)