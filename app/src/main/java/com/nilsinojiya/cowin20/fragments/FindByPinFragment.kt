package com.nilsinojiya.cowin20.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.nilsinojiya.cowin20.R
import com.nilsinojiya.cowin20.databinding.FragmentFindByPinBinding
import kotlinx.android.synthetic.main.fragment_find_by_pin.*
import java.text.SimpleDateFormat
import java.util.*

class FindByPinFragment : Fragment() {

    private val TAG = this::class.java.simpleName
    private var _binding: FragmentFindByPinBinding? = null
    private val binding get() = _binding!!
    private var navController: NavController? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentFindByPinBinding.inflate(inflater, container, false)

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
        datePickerDialog.datePicker.setBackgroundColor(ContextCompat.getColor(requireContext(),
            R.color.yellow_btn))

        binding.btnChangedate.setOnClickListener {
            datePickerDialog.show()
        }

        binding.btnSearch.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("PIN", binding.etPincode.text.toString().toInt())
            bundle.putString("DATE", tvDate.text.toString())
            navController!!.navigate(R.id.action_findByPinFragment_to_findByPinListFragment, bundle)
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

}