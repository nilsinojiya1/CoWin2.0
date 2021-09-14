package com.nilsinojiya.cowin20.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.nilsinojiya.cowin20.R
import com.nilsinojiya.cowin20.adapters.DistrictsAdapter
import com.nilsinojiya.cowin20.adapters.StatesAdapter
import com.nilsinojiya.cowin20.databinding.FragmentFindByStatesBinding
import com.nilsinojiya.cowin20.helper.ApiState
import com.nilsinojiya.cowin20.helper.Utility
import com.nilsinojiya.cowin20.models.District
import com.nilsinojiya.cowin20.models.State
import com.nilsinojiya.cowin20.repositorys.MainRepository
import com.nilsinojiya.cowin20.services.RetrofitService
import com.nilsinojiya.cowin20.viewModels.MainViewModel
import com.nilsinojiya.cowin20.viewModels.MyViewModelFactory
import kotlinx.android.synthetic.main.fragment_find_by_pin.*
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

class FindByStatesFragment : Fragment() {

    private val TAG = this::class.java.simpleName
    private var _binding: FragmentFindByStatesBinding? = null
    private val binding get() = _binding!!
    private var navController: NavController? = null
    private lateinit var viewModel: MainViewModel
    private val retrofitService = RetrofitService.getInstance()
    private lateinit var statesAdapter: StatesAdapter
    private lateinit var districtsAdapter: DistrictsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentFindByStatesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(MainViewModel::class.java)

        setSpinner()

        lifecycleScope.launchWhenStarted {
            viewModel._statesStateFlow.collect {
                when(it){
                    is ApiState.Loading -> {
                        Utility.hideLoading()
                        Utility.displayLoadingWithText(activity, "Please wait...", false)
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(), "Something Wrong, Please try again.", Toast.LENGTH_SHORT).show()
                    }
                    is ApiState.SuccessStates -> {
                        Utility.hideLoading()
                        statesAdapter = StatesAdapter(requireContext(), it.data.states)
                        binding.spinnerState.adapter = statesAdapter
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel._districtsStateFlow.collect {
                when(it){
                    is ApiState.Loading -> {
                        Utility.hideLoading()
                        Utility.displayLoadingWithText(activity, "Please wait...", false)
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(), "Something Wrong, Please try again.", Toast.LENGTH_SHORT).show()
                    }
                    is ApiState.SuccessDistricts -> {
                        Utility.hideLoading()
                        districtsAdapter = DistrictsAdapter(requireContext(), it.data.districts)
                        binding.spinnerDistrict.adapter = districtsAdapter
                    }
                }
            }
        }


        /*viewModel.states.observe(viewLifecycleOwner, Observer {
            statesAdapter = StatesAdapter(requireContext(), it.states)
            binding.spinnerState.adapter = statesAdapter
            Log.d(TAG, "onCreateView: ${it.states}")

        })

        viewModel.districts.observe(viewLifecycleOwner, Observer { 
            districtsAdapter = DistrictsAdapter(requireContext(), it.districts)
            binding.spinnerDistrict.adapter = districtsAdapter
            Log.d(TAG, "onCreateView: ${it.districts}")
            Utility.hideLoading()
        })*/


        binding.spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
               // Utility.displayLoadingWithText(activity, "Please wait...", false)
                var state = statesAdapter.getItem(position) as State
                viewModel.getDistricts(state.stateId.toString())
                Log.d(TAG, "onItemSelected: $position + ${statesAdapter.getItem(position)}")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                
            }

        }


        binding.tvDate.text = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(System.currentTimeMillis())

        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd-MM-yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.tvDate.text = sdf.format(cal.time)

        }

        val datePickerDialog =  DatePickerDialog(this.requireContext(), dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.setMinDate(System.currentTimeMillis())
        datePickerDialog.datePicker.setBackgroundColor(
            ContextCompat.getColor(requireContext(),
            R.color.yellow_btn))

        binding.btnChangedate.setOnClickListener {
            datePickerDialog.show()
        }
        
        binding.btnSearch.setOnClickListener {
            if(Utility.checkInternet(requireContext())){
                val district = binding.spinnerDistrict.selectedItem as District
                Log.d(TAG, "onCreateView: ${district.districtId}")
                val bundle = Bundle()
                bundle.putString("FROM", "FindByStatesFragment")
                bundle.putInt("DISTRICT", district.districtId)
                bundle.putString("DATE", tvDate.text.toString())
                navController!!.navigate(R.id.action_findByStatesFragment_to_findByPinListFragment, bundle)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setSpinner(){
        if(Utility.checkInternet(requireActivity())) {
            viewModel.getStates()
            viewModel.getDistricts("1")
        }
    }

}