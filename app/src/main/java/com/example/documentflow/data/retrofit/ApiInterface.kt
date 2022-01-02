package com.example.documentflow.data.retrofit

import com.example.documentflow.data.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    //Registration
    @POST("login")
    fun logInUser(@Body model : Login.LoginRequest): Call<Login.LoginResponse>

    //Letters
    @POST("letters")
    fun getLetterList(@Body model:Letters.LettersRequest):Call<Letters.LettersResponse>

    @POST("letter/{id}")
    fun getLetter(@Path("id") letterId:Int):Call<Letter.LetterResponse>

    //user data
    @POST("users/{id}")
    fun getUserData(@Path("id") userId:Int):Call<User.UserData>

    //departments
    @POST("departments")
    fun getDepartments():Call<Departments.DepartmentsResponse>

    //agreement
    @POST("letters/agreement")
    fun createAgreements(@Body model:Agreements.CreateAgreementsRequest):Call<Agreements.CreateAgreementsResponse>

    @POST("letters/agreements")
    fun getAgreements():Call<Agreements.GetAgreementsResponse>

    @POST("letters/agreement/{id}")
    fun getAgreement(@Path("id") agreementId:Int):Call<Agreements.GetAgreementResponse>


}