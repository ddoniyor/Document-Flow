package com.example.documentflow.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.documentflow.data.model.*

import com.example.documentflow.data.retrofit.ApiInterface
import com.example.documentflow.utils.ErrorHandler
import com.example.documentflow.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyViewModel(private val apiInterface: ApiInterface): ViewModel() {
    private companion object{
        const val TAG = "RegisterViewModel"
    }

    //login user
    val logInResponse = SingleLiveEvent<Login.LoginResponse>()
    val errorLogIn = MutableLiveData<String>()

    //get list of letter
    val lettersListResponse = MutableLiveData<Letters.LettersResponse>()
    val errorLetterList = MutableLiveData<String>()

    //get letter
    val letterResponse = MutableLiveData<Letter.LetterResponse>()
    var errorLetter = MutableLiveData<String>()

    //user data
    val userDataResponse = MutableLiveData<User.UserData>()
    val errorUserData = MutableLiveData<String>()

    //get departments
    val departmentsResponse = MutableLiveData<Departments.DepartmentsResponse>()
    val errorDepartments = MutableLiveData<String>()

    //agreements
    val createAgreementsResponse = MutableLiveData<Agreements.CreateAgreementsResponse>()
    val errorCreateAgreements = MutableLiveData<String>()

    val getAgreementsResponse = MutableLiveData<Agreements.GetAgreementsResponse>()
    var errorGetAgreements = MutableLiveData<String>()

    val getAgreementResponse  = MutableLiveData<Agreements.GetAgreementResponse>()
    val errorGetAgreement = MutableLiveData<String>()


    fun logInUser(model: Login.LoginRequest) {
        val response = apiInterface.logInUser(model)
        response.enqueue(object : Callback<Login.LoginResponse> {
            override fun onResponse(
                call: Call<Login.LoginResponse>,
                response: Response<Login.LoginResponse>
            ) {
                    logInResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<Login.LoginResponse>, t: Throwable) {
                val message = ErrorHandler().errorResponseHandler(t)
                errorLogIn.postValue(message)
            }
        })
    }

    fun getLetterList(model: Letters.LettersRequest){
        val response = apiInterface.getLetterList(model)
        response.enqueue(object :Callback<Letters.LettersResponse>{
            override fun onResponse(call: Call<Letters.LettersResponse>, response: Response<Letters.LettersResponse>) {
                lettersListResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<Letters.LettersResponse>, t: Throwable) {
                val message = ErrorHandler().errorResponseHandler(t)
                errorLetterList.postValue(message)
            }

        })
    }

    fun getLetter(letterId:Int){
        val response = apiInterface.getLetter(letterId)
        response.enqueue(object :Callback<Letter.LetterResponse>{
            override fun onResponse(
                call: Call<Letter.LetterResponse>,
                response: Response<Letter.LetterResponse>
            ) {
                letterResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<Letter.LetterResponse>, t: Throwable) {
                val message = ErrorHandler().errorResponseHandler(t)
                errorLetter.postValue(message)
            }

        })
    }

    fun getUserData(userId:Int){
        val response = apiInterface.getUserData(userId)
        response.enqueue(object :Callback<User.UserData>{
            override fun onResponse(call: Call<User.UserData>, response: Response<User.UserData>) {
                userDataResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<User.UserData>, t: Throwable) {
                val message = ErrorHandler().errorResponseHandler(t)
                errorUserData.postValue(message)
            }
        })
    }

    fun getDepartments(){
        val response = apiInterface.getDepartments()
        response.enqueue(object :Callback<Departments.DepartmentsResponse>{
            override fun onResponse(
                call: Call<Departments.DepartmentsResponse>,
                response: Response<Departments.DepartmentsResponse>
            ) {
                departmentsResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<Departments.DepartmentsResponse>, t: Throwable) {
                val message = ErrorHandler().errorResponseHandler(t)
                errorDepartments.postValue(message)
            }

        })
    }

    fun createAgreements(model:Agreements.CreateAgreementsRequest){
        val response =apiInterface.createAgreements(model)
        response.enqueue(object :Callback<Agreements.CreateAgreementsResponse>{
            override fun onResponse(
                call: Call<Agreements.CreateAgreementsResponse>,
                response: Response<Agreements.CreateAgreementsResponse>
            ) {
                createAgreementsResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<Agreements.CreateAgreementsResponse>, t: Throwable) {
                val message = ErrorHandler().errorResponseHandler(t)
                errorCreateAgreements.postValue(message)
            }

        })
    }

    fun getAgreements(){
        val response = apiInterface.getAgreements()
        response.enqueue(object :Callback<Agreements.GetAgreementsResponse>{
            override fun onResponse(
                call: Call<Agreements.GetAgreementsResponse>,
                response: Response<Agreements.GetAgreementsResponse>
            ) {
                getAgreementsResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<Agreements.GetAgreementsResponse>, t: Throwable) {
                val message = ErrorHandler().errorResponseHandler(t)
                errorGetAgreements.postValue(message)
            }

        })
    }
    fun getAgreement(agreementId:Int){
        val response = apiInterface.getAgreement(agreementId)
        response.enqueue(object :Callback<Agreements.GetAgreementResponse>{
            override fun onResponse(
                call: Call<Agreements.GetAgreementResponse>,
                response: Response<Agreements.GetAgreementResponse>
            ) {
                getAgreementResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<Agreements.GetAgreementResponse>, t: Throwable) {
                val message = ErrorHandler().errorResponseHandler(t)
                errorGetAgreement.postValue(message)
            }
        })
    }


}