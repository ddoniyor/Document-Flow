package com.example.documentflow.data.model

import com.google.gson.annotations.SerializedName

object Agreements {

    data class CreateAgreementsRequest(
        var department_id:Int?=null,
        var letter_id:Int?=null
    )
    data class CreateAgreementsResponse(
        @SerializedName("code")
        var code:Int?=null,
        @SerializedName("message")
        var message: String? = null,
        @SerializedName("time")
        var time :String?=null
    )

    data class GetAgreementsResponse(
        @SerializedName("code")
        var code: Int? = null,
        @SerializedName("message")
        var message: String? = null,
        @SerializedName("time")
        var time: String? = null,
        @SerializedName("payload")
        var payload: List<AgreementsPayload>? = null
    )
    data class GetAgreementResponse(
        @SerializedName("code")
        var code: Int? = null,
        @SerializedName("message")
        var message: String? = null,
        @SerializedName("time")
        var time: String? = null,
        @SerializedName("payload")
        var payload: AgreementsPayload? = null
    )

    data class AgreementsPayload(
        @SerializedName("id")
        var agreementId:Int?=null,
        @SerializedName("department_id")
        var departmentId:Int?=null,
        @SerializedName("letter")
        var letter:AgreementsLetter,
        @SerializedName("viewed")
        var viewed:Boolean?=null,
        @SerializedName("agreed_at")
        var agreedAt:String?=null
    )
    data class AgreementsLetter(
        @SerializedName("id")
        var letterId:Int?=null,
        @SerializedName("name")
        var name :String?=null,
        @SerializedName("entry_date")
        var entryDate:String?=null,
        @SerializedName("distribution_date")
        var distributionDate:String?=null
    )


}