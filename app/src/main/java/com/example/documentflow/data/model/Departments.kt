package com.example.documentflow.data.model

import com.google.gson.annotations.SerializedName

object Departments {
    data class DepartmentsResponse(
        @SerializedName("code")
        var code:Int?=null,
        @SerializedName("message")
        var message: String? = null,
        @SerializedName("time")
        var time :String?=null,
        @SerializedName("payload")
        var payload: List<DepartmentsList>?=null
    )

    data class DepartmentsList(
        @SerializedName("id")
        var id:Int?=null,
        @SerializedName("name")
        var name:String?=null,
        @SerializedName("internal_number")
        var internalNumber:String?=null,
        @SerializedName("phone")
        var phone:String?=null
    )
}