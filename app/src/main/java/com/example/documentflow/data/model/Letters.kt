package com.example.documentflow.data.model

import com.google.gson.annotations.SerializedName

object Letters {
    data class LettersRequest(
        var rows_offset:Int?=null,
        var sender:String?=null,
        var name:String?=null,
        var rows_limit:Int?=null
    )
    data class LettersResponse(
        @SerializedName("code")
        var code:Int?=null,
        @SerializedName("message")
        var message: String? = null,
        @SerializedName("time")
        var time :String?=null,
        @SerializedName("payload")
        var payload: List<LettersList>?=null

    )
    data class LettersList(
        @SerializedName("id")
        var id :Int?=null,
        @SerializedName("name")
        var name:String?=null,
        @SerializedName("sender")
        var sender:String?=null,
        /*@SerializedName("document_type")
        var docType:String?=null,*/
        @SerializedName("entry_date")
        var entryDate:String?=null,
        @SerializedName("distribution_date")
        var distributionDate:String?=null
    )
}