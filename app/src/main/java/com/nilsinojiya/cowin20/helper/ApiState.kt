package com.nilsinojiya.cowin20.helper

import com.nilsinojiya.cowin20.models.Districts
import com.nilsinojiya.cowin20.models.Sessions
import com.nilsinojiya.cowin20.models.States
import retrofit2.Call

sealed class ApiState{
    object Loading : ApiState()
    class Failure(val mssg: Throwable) : ApiState()
    class SuccessSession(val data: Sessions) : ApiState()
    class SuccessStates(val data: States) : ApiState()
    class SuccessDistricts(val data: Districts) : ApiState()
    object Empty: ApiState()
}
