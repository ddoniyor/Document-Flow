package com.example.documentflow.data.model

import com.google.gson.annotations.SerializedName

object Letter {
    data class LetterResponse(
        @SerializedName("code")
        var code:Int?=null,
        @SerializedName("message")
        var message: String? = null,
        @SerializedName("time")
        var time :String?=null,
        @SerializedName("payload")
        var payload: LetterDescription?=null
    )
    data class LetterDescription(
        @SerializedName("id")
        var id :Int?=null,
        @SerializedName("name")
        var name:String?=null,
        @SerializedName("sender")
        var sender:String?=null,
        @SerializedName("document_type")
        var docType:LetterType?=null,
        @SerializedName("registration_number")
        var registrationNumber:String?=null,
        @SerializedName("entry_date")
        var entryDate:String?=null,
        @SerializedName("outgoing_number")
        var outgoingNumber:String?=null,
        @SerializedName("distribution_date")
        var distributionDate:String?=null,
        @SerializedName("content")
        var content:String?=null

    )
    data class LetterType(
        @SerializedName("type")
        var type:String?=null
    )
}