package com.nilsinojiya.cowin20.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Sessions (
    @SerializedName("sessions") var centers : @RawValue List<Center>
):Parcelable
