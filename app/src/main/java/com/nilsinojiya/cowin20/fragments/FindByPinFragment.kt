package com.nilsinojiya.cowin20.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.location.*
import com.nilsinojiya.cowin20.R
import com.nilsinojiya.cowin20.databinding.FragmentFindByPinBinding
import com.nilsinojiya.cowin20.helper.Utility
import com.vmadalin.easypermissions.EasyPermissions
import kotlinx.android.synthetic.main.fragment_find_by_pin.*
import java.text.SimpleDateFormat
import java.util.*

class FindByPinFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    private val TAG = this::class.java.simpleName
    private var _binding: FragmentFindByPinBinding? = null
    private val binding get() = _binding!!
    private var navController: NavController? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =  FragmentFindByPinBinding.inflate(inflater, container, false)
        Utility.checkInternet(requireContext())
        binding.tvDate.text = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(System.currentTimeMillis())
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        requestLocationPermission()
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
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.datePicker.setBackgroundColor(ContextCompat.getColor(requireContext(),
            R.color.yellow_btn))

        binding.btnChangedate.setOnClickListener {
            datePickerDialog.show()
        }

        binding.btnSearch.setOnClickListener {
            if(Utility.checkInternet(requireActivity())){
                if(binding.etPincode.text?.trim()?.length == 6){
                    val bundle = Bundle()
                    bundle.putString("FROM", "FindByPinFragment")
                    bundle.putInt("PIN", binding.etPincode.text.toString().toInt())
                    bundle.putString("DATE", tvDate.text.toString())
                    navController!!.navigate(R.id.action_findByPinFragment_to_findByPinListFragment, bundle)
                } else{
                    Toast.makeText(context, "Please Enter Valid pincode.", Toast.LENGTH_SHORT).show()
                }
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

    private fun getPincode(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())

        val addresses: MutableList<Address> = geocoder.getFromLocation(latitude, longitude, 1)

        //val address = addresses[0].getAddressLine(0)
        //val city = addresses[0].locality
        //val state = addresses[0].adminArea
        //val country = addresses[0].countryName
        val postalCode = addresses[0].postalCode
        //val knownName = addresses[0].featureName

        return postalCode.toString()
    }


    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "Allow Location Permission for better use.",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private fun isLocationEnabled():Boolean{
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(){
        if(hasLocationPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient!!.lastLocation.addOnCompleteListener {
                    val location:Location? = it.result
                    if(location == null){
                        Log.d(TAG, "getLastLocation: ")
                        NewLocationData()
                    }else{
                        Log.d(TAG ,"Your Location:"+ location.longitude)
                        binding.etPincode.setText(getPincode(location.latitude, location.longitude))
                    }
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun NewLocationData(){
        Log.d(TAG, "NewLocationData: ")
        val locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback,Looper.myLooper()!!
        )
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            Log.d(TAG,"your last last location: "+ lastLocation.longitude.toString())
            binding.etPincode.setText(getPincode(lastLocation.latitude, lastLocation.longitude))
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(), perms)) {
            //SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        getLastLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}