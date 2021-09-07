package com.nilsinojiya.cowin20.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nilsinojiya.cowin20.databinding.ItemCenterBinding
import com.nilsinojiya.cowin20.models.Center
import com.nilsinojiya.cowin20.models.Sessions

class CenterAdapter: RecyclerView.Adapter<MainViewHolder>() {
    private val TAG = this::class.java.simpleName

    var sessions = mutableListOf<Sessions>()

    fun setCenterList(center: Center) {
        this.sessions = center.sessions.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCenterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val center = sessions[position]
        holder.binding.tvName.text = center.name
        Log.d(TAG, "onBindViewHolder: ${center.name}")
    }

    override fun getItemCount(): Int {
        return sessions.size
    }
}

class MainViewHolder(val binding: ItemCenterBinding) : RecyclerView.ViewHolder(binding.root)