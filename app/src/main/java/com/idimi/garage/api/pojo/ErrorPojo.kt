package com.idimi.garage.api.pojo

import com.google.gson.annotations.SerializedName

data class ErrorPojo (
    @SerializedName("field") var field: String? = null,
    @SerializedName("message") var message: String? = null
)