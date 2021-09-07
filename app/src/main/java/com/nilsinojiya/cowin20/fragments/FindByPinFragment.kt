package com.nilsinojiya.cowin20.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.datepicker.MaterialDatePicker
import com.nilsinojiya.cowin20.R
import com.nilsinojiya.cowin20.adapters.CenterAdapter
import com.nilsinojiya.cowin20.databinding.FragmentFindByPinBinding
import com.nilsinojiya.cowin20.databinding.FragmentHomeBinding
import com.nilsinojiya.cowin20.repositorys.MainRepository
import com.nilsinojiya.cowin20.services.RetrofitService
import com.nilsinojiya.cowin20.viewModels.MainViewModel
import com.nilsinojiya.cowin20.viewModels.MyViewModelFactory
import kotlinx.android.synthetic.main.fragment_find_by_pin.*
import java.text.SimpleDateFormat
import java.util.*

class FindByPinFragment : Fragment() {

    private val TAG = this::class.java.simpleName
    private var _binding: FragmentFindByPinBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: MainViewModel
    private var navController: NavController? = null
    private val retrofitService = RetrofitService.getInstance()
    private val adapter = CenterAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentFindByPinBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(MainViewModel::class.java)
        //recyclerViewMain.adapter = adapter

        viewModel.center.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onCreate: $it")
            adapter.setCenterList(it)
            if(it.centers.size > 0)
            {
                val bundle = Bundle()
                bundle.putParcelable("CENTERS", it) // Serializable Object

                navController!!.navigate(R.id.action_findByPinFragment_to_findByPinListFragment, bundle)
            }
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
        })


        binding.tvDate.text = SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis())

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd-MM-yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.tvDate.text = sdf.format(cal.time)

        }

        binding.btnChangedate.setOnClickListener {
            DatePickerDialog(this.requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.btnSearch.setOnClickListener {
            viewModel.findByPin(binding.etPincode.text.toString().toInt(), tvDate.text.toString())
        }


        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}