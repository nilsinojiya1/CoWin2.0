package com.nilsinojiya.cowin20.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nilsinojiya.cowin20.adapters.CenterAdapter
import com.nilsinojiya.cowin20.databinding.FragmentFindByPinListBinding
import com.nilsinojiya.cowin20.helper.Utility
import com.nilsinojiya.cowin20.repositorys.MainRepository
import com.nilsinojiya.cowin20.services.RetrofitService
import com.nilsinojiya.cowin20.viewModels.MainViewModel
import com.nilsinojiya.cowin20.viewModels.MyViewModelFactory
import androidx.core.app.NotificationManagerCompat
import android.app.Notification
import androidx.core.app.NotificationCompat
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.nilsinojiya.cowin20.R
import com.nilsinojiya.cowin20.helper.App
import com.nilsinojiya.cowin20.helper.SlotCheckWorker
import java.util.*
import java.util.concurrent.TimeUnit


class FindByPinListFragment : Fragment() {
    private val TAG = this::class.java.simpleName
    private lateinit var _binding: FragmentFindByPinListBinding
    private val binding get() = _binding
    private val adapter = CenterAdapter()
    private lateinit var viewModel: MainViewModel
    private var query: String = ""
    private var currentDate: String =""
    private val retrofitService = RetrofitService.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFindByPinListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(
            MainViewModel::class.java)
        Utility.checkInternet(requireContext())
        binding.recyclerViewMain.adapter = adapter
        currentDate = requireArguments().getString("DATE","09/09/2021").toString()

        setList(currentDate)

        viewModel.sessions.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onCreate: $it")
                adapter.setCenterList(it)
                adapter.notifyDataSetChanged()
                adapter.filter.filter(query)
            if(adapter.itemCount > 0){
                binding.lottieLoading.visibility = View.GONE
                binding.recyclerViewMain.visibility = View.VISIBLE
                binding.hsvChipGroup.visibility = View.VISIBLE
                binding.llNoData.visibility = View.GONE

            } else {
                binding.lottieLoading.visibility = View.GONE
                binding.recyclerViewMain.visibility = View.GONE
                binding.llNoData.visibility = View.VISIBLE
                binding.hsvChipGroup.visibility = View.GONE
            }
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
        })


        binding.chipFree.setOnClickListener {
            if(binding.chipFree.isChecked){
                //adapter.filter.filter("free")
                addQuery("free")
            } else{
                //adapter.filter.filter("")
                removeQuery("free")
            }
        }
        binding.chipAge45.setOnClickListener {
            if(binding.chipAge45.isChecked){
                //adapter.filter.filter("45+")
                addQuery("45")
            } else{
                //adapter.filter.filter("")
                removeQuery("45")
            }
        }
        binding.chipAge18.setOnClickListener {
            if(binding.chipAge18.isChecked){
                //adapter.filter.filter("18+")
                addQuery("18")
            } else {
                //adapter.filter.filter("")
                removeQuery("18")
            }
        }
        binding.chipCOVAXIN.setOnClickListener {
            if(binding.chipCOVAXIN.isChecked){
                //adapter.filter.filter("COVAXIN")
                addQuery("COVAXIN")
            } else {
                //adapter.filter.filter("")
                removeQuery("COVAXIN")
            }
        }
        binding.chipCOVISHIELD.setOnClickListener {
            if(binding.chipCOVISHIELD.isChecked){
                //adapter.filter.filter("COVISHIELD")
                addQuery("COVISHIELD")
            } else {
                //adapter.filter.filter("")
                removeQuery("COVISHIELD")
            }
        }
        binding.chip1Dose.setOnClickListener {
            if(binding.chip1Dose.isChecked){
                //adapter.filter.filter("COVISHIELD")
                addQuery("1st_Dose")
            } else {
                //adapter.filter.filter("")
                removeQuery("1st_Dose")
            }
        }
        binding.chip2Dose.setOnClickListener {
            if(binding.chip2Dose.isChecked){
                //adapter.filter.filter("COVISHIELD")
                addQuery("2nd_Dose")
            } else {
                //adapter.filter.filter("")
                removeQuery("2nd_Dose")
            }
        }

        binding.swipeRefreshList.setOnRefreshListener {
            if(Utility.checkInternet(requireContext())) {
                setList(currentDate)
            } else {
                binding.swipeRefreshList.isRefreshing = false
            }
        }

        binding.ivBackArrow.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.ivBellAlert.setOnClickListener {
            startSlotCheckWorker()
        }

        binding.ibDateBack.setOnClickListener {
            //Log.d(TAG, "onCreateView: ${Utility.dateToString(Utility.decrementDateByOne(Utility.stringToDate(currentDate)))}")
            val date = Utility.dateToString(Utility.decrementDateByOne(Utility.stringToDate(currentDate)))
            setList(date)

        }

        binding.ibDateNext.setOnClickListener {
            //Log.d(TAG, "onCreateView: ${Utility.dateToString(Utility.incrementDateByOne(Utility.stringToDate(currentDate)))}")
            val date = Utility.dateToString(Utility.incrementDateByOne(Utility.stringToDate(currentDate)))
            setList(date)
        }

        return _binding.root
    }

    private fun addFilter(query: String){
        adapter.filter.filter(query)
    }

    private fun addQuery(string: String){
        if(!query.contains(string)){
            query = "$query$string"
            addFilter(query)
        }

    }

    private fun removeQuery(string: String){
        query = query.replace("$string", "")
        addFilter(query)
    }

    private fun setList(date: String){
        if(Utility.checkInternet(requireContext())){
            binding.lottieLoading.visibility = View.VISIBLE
            if(requireArguments().getString("FROM") == "FindByPinFragment"){
                viewModel.findByPin(requireArguments().getInt("PIN",0), date)
            } else if(requireArguments().getString("FROM") == "FindByStatesFragment") {
                viewModel.findByDistrict(requireArguments().getInt("DISTRICT",0), date)
            }
            currentDate = date
            binding.tvDate.text = currentDate
        }
        binding.swipeRefreshList.isRefreshing = false
    }

    private fun startSlotCheckWorker(){
        val periodicWorkRequest = PeriodicWorkRequest.Builder(SlotCheckWorker::class.java, 15, TimeUnit.MINUTES)
            .addTag("SLOTJOB")
            .build()

        WorkManager.getInstance(requireContext()).cancelAllWorkByTag("SLOTJOB")
        WorkManager.getInstance(requireContext()).enqueue(periodicWorkRequest)
    }
}