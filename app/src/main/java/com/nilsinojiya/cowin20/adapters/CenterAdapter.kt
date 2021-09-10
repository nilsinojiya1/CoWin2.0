package com.nilsinojiya.cowin20.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.nilsinojiya.cowin20.databinding.ItemCenterBinding
import com.nilsinojiya.cowin20.models.Center
import com.nilsinojiya.cowin20.models.Sessions
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CenterAdapter: RecyclerView.Adapter<MainViewHolder>(), Filterable {
    private val TAG = this::class.java.simpleName

    var centers = mutableListOf<Center>()
    var centersFilterList = mutableListOf<Center>()

    fun setCenterList(sessions: Sessions) {
        this.centers = sessions.centers.toMutableList()
        this.centersFilterList = sessions.centers.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCenterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val center = centersFilterList[position]
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
        holder.binding.tvVaccine.text = center.vaccine
        //Log.d(TAG, "onBindViewHolder: ${center.name}")
    }

    override fun getItemCount(): Int {
        return centersFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                Log.d(TAG, "performFiltering: $charSearch Thread: ${Thread.currentThread().name}")
                if (charSearch.isEmpty()) {
                    centersFilterList = centers
                } else {
                    val resultList = mutableListOf<Center>()
                    for (row in centers) {
                        var isValid = true
                        if (constraint.toString().contains("free") && isValid) {
                            if(!row.feeType.equals("Free")){
                                isValid = false
                            }
                        }
                        if(constraint.toString().contains("18") && isValid){
                            if(row.minAgeLimit != 18){
                                isValid = false
                            }
                        }
                        if(constraint.toString().contains("45") && isValid){
                            if(row.minAgeLimit != 45){
                                isValid = false

                            }
                        }
                        if(!(constraint.toString().contains("COVISHIELD") && constraint.toString().contains("COVAXIN"))){
                            if(constraint.toString().contains("COVISHIELD") && isValid){
                                if(!row.vaccine.equals("COVISHIELD")){
                                    isValid = false
                                }
                            }
                            if(constraint.toString().contains("COVAXIN") && isValid){
                                if(!row.vaccine.equals("COVAXIN")){
                                    isValid = false
                                }
                            }
                        }
                        if(constraint.toString().contains("2nd_Dose") && isValid){
                            if(row.availableCapacityDose2 <= 0){
                                isValid = false
                            }
                        }
                        if(constraint.toString().contains("1st_Dose") && isValid){
                            if(row.availableCapacityDose1 <= 0){
                                isValid = false
                            }
                        }
                        if(isValid){
                            resultList.add(row)
                        }
                    }
                    centersFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = centersFilterList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                centersFilterList = results?.values as ArrayList<Center>
                notifyDataSetChanged()
            }

        }
    }


}

class MainViewHolder(val binding: ItemCenterBinding) : RecyclerView.ViewHolder(binding.root)