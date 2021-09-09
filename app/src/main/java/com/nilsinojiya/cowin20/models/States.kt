package com.nilsinojiya.cowin20.models

import com.google.gson.annotations.SerializedName

data class States(
    @SerializedName("states") var states : List<State>,
    @SerializedName("ttl") var total : Int
)


data class State(
    @SerializedName("state_id") var stateId : Int,
    @SerializedName("state_name") var stateName : String
)
