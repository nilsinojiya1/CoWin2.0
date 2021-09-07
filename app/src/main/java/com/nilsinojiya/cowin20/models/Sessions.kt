package com.nilsinojiya.cowin20.models

import com.google.gson.annotations.SerializedName


data class Sessions (

    @SerializedName("center_id") var centerId : Int,
    @SerializedName("name") var name : String,
    @SerializedName("address") var address : String,
    @SerializedName("state_name") var stateName : String,
    @SerializedName("district_name") var districtName : String,
    @SerializedName("block_name") var blockName : String,
    @SerializedName("pincode") var pincode : Int,
    @SerializedName("from") var from : String,
    @SerializedName("to") var to : String,
    @SerializedName("lat") var lat : Int,
    @SerializedName("long") var long : Int,
    @SerializedName("fee_type") var feeType : String,
    @SerializedName("session_id") var sessionId : String,
    @SerializedName("date") var date : String,
    @SerializedName("available_capacity") var availableCapacity : Int,
    @SerializedName("available_capacity_dose1") var availableCapacityDose1 : Int,
    @SerializedName("available_capacity_dose2") var availableCapacityDose2 : Int,
    @SerializedName("fee") var fee : String,
    @SerializedName("min_age_limit") var minAgeLimit : Int,
    @SerializedName("allow_all_age") var allowAllAge : Boolean,
    @SerializedName("vaccine") var vaccine : String,
    @SerializedName("slots") var slots : List<String>

)
