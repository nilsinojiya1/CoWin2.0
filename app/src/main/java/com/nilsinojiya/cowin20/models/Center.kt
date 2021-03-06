package com.nilsinojiya.cowin20.models

import com.google.gson.annotations.SerializedName

data class Center (
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
    @SerializedName("max_age_limit") var maxAgeLimit : Int,
    @SerializedName("allow_all_age") var allowAllAge : Boolean,
    @SerializedName("vaccine") var vaccine : String,
    @SerializedName("slots") var slots : List<String>
)





/*"center_id": 363619,
"name": "PP Unit Anand UHC",
"address": "15 Narayan Nivas Near Old CK Hall Mafer Road Anand Taluko And District Anand",
"state_name": "Gujarat",
"district_name": "Anand",
"block_name": "Anand",
"pincode": 388001,
"from": "09:30:00",
"to": "15:30:00",
"lat": 22,
"long": 72,
"fee_type": "Free",
"session_id": "6744fa4d-411a-4b33-a877-9b8574d86caf",
"date": "07-09-2021",
"available_capacity": 147,
"available_capacity_dose1": 49,
"available_capacity_dose2": 98,
"fee": "0",
"min_age_limit": 18,
"allow_all_age": true,
"vaccine": "COVAXIN",
"slots": [
"09:30AM-10:30AM",
"10:30AM-11:30AM",
"11:30AM-12:30PM",
"12:30PM-03:30PM"
]*/
