package com.example.documentflow.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.documentflow.data.retrofit.ApiInterface
import com.example.documentflow.viewmodel.MyViewModel

import java.lang.IllegalArgumentException

class ViewModelFactory(private val apiInterface: ApiInterface):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MyViewModel::class.java)){
            MyViewModel(this.apiInterface) as T
        }else{
            throw IllegalArgumentException("Unknown RegisterViewModel Class")
        }
    }
}