package com.nilsinojiya.cowin20.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.nilsinojiya.cowin20.R
import com.nilsinojiya.cowin20.adapters.CenterAdapter
import com.nilsinojiya.cowin20.databinding.FragmentFindByPinBinding
import com.nilsinojiya.cowin20.databinding.FragmentFindByPinListBinding
import com.nilsinojiya.cowin20.models.Sessions

class FindByPinListFragment : Fragment() {
    private val TAG = this::class.java.simpleName
    private lateinit var _binding: FragmentFindByPinListBinding
    private val binding get() = _binding!!
    private val adapter = CenterAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFindByPinListBinding.inflate(inflater, container, false)


        val sessions: Sessions = arguments!!.getParcelable<Sessions>("CENTERS") as Sessions
        Log.d(TAG, "onCreateView: ${sessions.toString()}")
        adapter.setCenterList(sessions)

        binding.recyclerViewMain.adapter = adapter
        adapter.notifyDataSetChanged()

        return _binding.root
    }

}