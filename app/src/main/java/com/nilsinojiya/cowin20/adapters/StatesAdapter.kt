package com.nilsinojiya.cowin20.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.nilsinojiya.cowin20.R
import com.nilsinojiya.cowin20.models.State

class StatesAdapter(val context: Context, var states: List<State>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return states.size
    }

    override fun getItem(position: Int): Any {
        return states[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_spinner_title_only, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.title.text = states[position].stateName

        return view
    }

    private class ItemHolder(row: View?) {
        val title: TextView = row?.findViewById(R.id.tvTitle) as TextView

    }
}