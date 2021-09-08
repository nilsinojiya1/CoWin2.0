package com.nilsinojiya.cowin20.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.nilsinojiya.cowin20.R
import com.nilsinojiya.cowin20.adapters.CenterAdapter
import com.nilsinojiya.cowin20.databinding.FragmentFindByPinBinding
import com.nilsinojiya.cowin20.databinding.FragmentFindByPinListBinding
import com.nilsinojiya.cowin20.models.Sessions
import com.nilsinojiya.cowin20.repositorys.MainRepository
import com.nilsinojiya.cowin20.services.RetrofitService
import com.nilsinojiya.cowin20.viewModels.MainViewModel
import com.nilsinojiya.cowin20.viewModels.MyViewModelFactory
import kotlinx.android.synthetic.main.fragment_find_by_pin.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.annotation.NonNull

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import kotlinx.android.synthetic.main.fragment_find_by_pin_list.*


class FindByPinListFragment : Fragment() {
    private val TAG = this::class.java.simpleName
    private lateinit var _binding: FragmentFindByPinListBinding
    private val binding get() = _binding!!
    private val adapter = CenterAdapter()
    private lateinit var viewModel: MainViewModel
    private val retrofitService = RetrofitService.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFindByPinListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(
            MainViewModel::class.java)

        binding.recyclerViewMain.adapter = adapter

        viewModel.findByPin(requireArguments().getInt("PIN",0), requireArguments().getString("DATE","0").toString())


        viewModel.sessions.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onCreate: $it")
                adapter.setCenterList(it)
                adapter.notifyDataSetChanged()
            if(adapter.itemCount > 0){
                binding.lottieLoading.visibility = View.GONE
                binding.recyclerViewMain.visibility = View.VISIBLE
                binding.imgListEmpty.visibility = View.GONE
            } else {
                binding.lottieLoading.visibility = View.GONE
                binding.recyclerViewMain.visibility = View.GONE
                binding.imgListEmpty.visibility = View.VISIBLE
            }
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
        })

        return _binding.root
    }

}