package com.nilsinojiya.cowin20.models

import com.google.gson.annotations.SerializedName

data class District(
    @SerializedName("district_id") var districtId : Int,
    @SerializedName("district_name") var districtName : String
)

data class Districts(
    @SerializedName("districts") var districts : List<District>,
    @SerializedName("ttl") var total : Int
)
