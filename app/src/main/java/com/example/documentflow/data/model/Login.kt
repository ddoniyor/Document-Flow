package com.example.documentflow.data.model

import com.google.gson.annotations.SerializedName

object Login {
    data class LoginRequest(
        @SerializedName("email")
        var email: String? = null,
        @SerializedName("password")
        var password: String? = null,
        )

    data class LoginResponse(
        @SerializedName("code")
        var code:Int?=null,
        @SerializedName("message")
        var message: String? = null,
        @SerializedName("time")
        var time :String?=null,
        @SerializedName("payload")
        var payload:Payload?=null
    )
    data class Payload(
        @SerializedName("token")
        var token:String?=null,
        @SerializedName("role")
        var role:String?=null
    )

}