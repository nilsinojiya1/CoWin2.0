package com.nilsinojiya.cowin20.models

import com.google.gson.annotations.SerializedName


data class Sessions (
    @SerializedName("sessions") var centers : List<Center>
)
