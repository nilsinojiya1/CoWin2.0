package com.nilsinojiya.cowin20.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.nilsinojiya.cowin20.adapters.CenterAdapter
import com.nilsinojiya.cowin20.databinding.FragmentFindByPinListBinding
import com.nilsinojiya.cowin20.repositorys.MainRepository
import com.nilsinojiya.cowin20.services.RetrofitService
import com.nilsinojiya.cowin20.viewModels.MainViewModel
import com.nilsinojiya.cowin20.viewModels.MyViewModelFactory
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.nilsinojiya.cowin20.R
import com.nilsinojiya.cowin20.helper.*
import com.nilsinojiya.cowin20.models.Sessions
import kotlinx.android.synthetic.main.dialog_notification_setting.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.awaitResponse
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
    private var pref = SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFindByPinListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(
            MainViewModel::class.java)
        pref.init(requireContext())
        Utility.checkInternet(requireContext())
        binding.recyclerViewMain.adapter = adapter
        currentDate = requireArguments().getString("DATE","09/09/2021").toString()

        setList(currentDate)

        /*viewModel.sessions.observe(viewLifecycleOwner, Observer {
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
        })*/

        lifecycleScope.launchWhenStarted {
            viewModel._sessionsStateFlow.collect {
                when(it){
                    is ApiState.Loading -> listLoading()
                    is ApiState.Failure -> listError()
                    is ApiState.Empty -> listEmpty()
                    is ApiState.SuccessSession -> listSuccess(it.data)
                }
            }
        }

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
            openSettingDialog()
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

    private fun openSettingDialog(){
        val view = View.inflate(requireContext(), R.layout.dialog_notification_setting, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)

        val isNotificationOn = pref.getIsNotificationOn()
        val vaccine = pref.getVaccine()
        val dose = pref.getDose()

        if(isNotificationOn){
            view.chipOn.isChecked = true
        } else {
            view.chipOff.isChecked = true
        }

        if(vaccine?.toUpperCase().equals("COVAXIN")) {
            view.chipCOVAXIN.isChecked = true
        } else {
            view.chipCOVISHIELD.isChecked = true
        }

        if(dose==1){
            view.chip1Dose.isChecked = true
        } else {
            view.chip2Dose.isChecked = true
        }

        view.etPincode.setText(pref.getPin().toString())

        view.btnClose.setOnClickListener {
            dialog.dismiss()

        }
        view.btnSave.setOnClickListener {
            if(!TextUtils.isEmpty(view.etPincode.text) && view.etPincode.text.toString().trim().length == 6){
                pref.setPin(view.etPincode.text.toString().toInt())
                startSlotCheckWorker()
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please Enter Valid pincode", Toast.LENGTH_SHORT).show()
            }
        }

        view.chipOff.setOnClickListener {
            pref.setIsNotificationOn(false)
        }
        view.chipOn.setOnClickListener {
            pref.setIsNotificationOn(true)
        }
        view.chipCOVAXIN.setOnClickListener {
            pref.setVaccine("COVAXIN")
        }
        view.chipCOVISHIELD.setOnClickListener {
            pref.setVaccine("COVISHIELD")
        }
        view.chip1Dose.setOnClickListener {
            pref.setDose(1)
        }
        view.chip2Dose.setOnClickListener {
            pref.setDose(2)
        }
    }

    private fun startSlotCheckWorker(){
        WorkManager.getInstance(requireContext()).cancelAllWorkByTag("SLOTJOB")
        if (pref.getIsNotificationOn()) {
            val periodicWorkRequest = PeriodicWorkRequest.Builder(SlotCheckWorker::class.java, 15, TimeUnit.MINUTES)
                .addTag("SLOTJOB")
                .build()
            WorkManager.getInstance(requireContext()).enqueue(periodicWorkRequest)
        }
    }

    private fun listLoading(){
        binding.lottieLoading.visibility = View.VISIBLE
        binding.recyclerViewMain.visibility = View.GONE
        binding.hsvChipGroup.visibility = View.GONE
        binding.llNoData.visibility = View.GONE
    }
    private fun listEmpty(){
        binding.lottieLoading.visibility = View.GONE
        binding.recyclerViewMain.visibility = View.GONE
        binding.hsvChipGroup.visibility = View.GONE
        binding.llNoData.visibility = View.VISIBLE
        binding.tvNoDataCaption.text = getString(R.string.no_slots_available_please_try_later)
    }
    private fun listSuccess(data: Sessions) {
        binding.lottieLoading.visibility = View.GONE
        binding.recyclerViewMain.visibility = View.VISIBLE
        binding.hsvChipGroup.visibility = View.VISIBLE
        binding.llNoData.visibility = View.GONE
        adapter.setCenterList(data)
        adapter.notifyDataSetChanged()
        adapter.filter.filter(query)
    }
    private fun listError(){
        binding.lottieLoading.visibility = View.GONE
        binding.recyclerViewMain.visibility = View.GONE
        binding.hsvChipGroup.visibility = View.GONE
        binding.llNoData.visibility = View.VISIBLE
        binding.tvNoDataCaption.text = getString(R.string.something_wrong)
        Toast.makeText(requireContext(), "Something wrong please try again", Toast.LENGTH_SHORT).show()
    }

}