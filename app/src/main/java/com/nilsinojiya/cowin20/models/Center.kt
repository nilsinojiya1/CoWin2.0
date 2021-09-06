package com.nilsinojiya.cowin20.models

import com.google.gson.annotations.SerializedName

data class Center (

    @SerializedName("sessions") var sessions : List<Sessions>

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
