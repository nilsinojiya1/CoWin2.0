package com.nilsinojiya.cowin20

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nilsinojiya.cowin20.adapters.CenterAdapter
import com.nilsinojiya.cowin20.databinding.ActivityMainBinding
import com.nilsinojiya.cowin20.repositorys.MainRepository
import com.nilsinojiya.cowin20.services.RetrofitService
import com.nilsinojiya.cowin20.viewModels.MainViewModel
import com.nilsinojiya.cowin20.viewModels.MyViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    private val retrofitService = RetrofitService.getInstance()
    private val adapter = CenterAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(MainViewModel::class.java)
        recyclerViewMain.adapter = adapter

        viewModel.center.observe(this, Observer {
            Log.d(TAG, "onCreate: $it")
            adapter.setCenterList(it)
        })
        viewModel.errorMessage.observe(this, Observer {
        })

        btnSearch.setOnClickListener {
            val pincode = etPincode.text.toString().toInt()
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            val currentDate = sdf.format(Date()).toString()
            viewModel.findByPin(pincode, "07-09-2021")
        }

    }
}