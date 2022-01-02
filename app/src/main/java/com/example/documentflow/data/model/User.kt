package com.example.documentflow.data.model

import com.google.gson.annotations.SerializedName

object User {
    data class UserData(
        @SerializedName("code")
        var code:Int?=null,
        @SerializedName("message")
        var message:String?=null,
        @SerializedName("time")
        var time:String?=null,
        @SerializedName("payload")
        var payload:UserDataResponse?=null
    )
    data class UserDataResponse(
        @SerializedName("id")
        var userId:Int?=null,
        @SerializedName("full_name")
        var fullName:String?=null,
        @SerializedName("role")
        var role :UserRole?=null,
        @SerializedName("email")
        var email:String?=null,
        @SerializedName("department")
        var department:UserDepartment
    )
    data class UserRole(
        @SerializedName("role")
        var role:String?=null,
    )
    data class UserDepartment(
        @SerializedName("name")
        var name:String?=null
    )
}