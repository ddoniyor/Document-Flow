package com.example.documentflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.documentflow.data.model.Login
import com.example.documentflow.data.retrofit.RetrofitBuilder
import com.example.documentflow.databinding.ActivityLoginBinding
import com.example.documentflow.utils.SharedPreferencesHelper
import com.example.documentflow.viewmodel.MyViewModel
import com.example.documentflow.viewmodel.factory.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private companion object{
        const val TAG = "Login Activity"
    }
    private fun log(message: String) {
        Log.d(TAG, message)
    }
    private lateinit var binding : ActivityLoginBinding
    private lateinit var myViewModel: MyViewModel
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setUpViewModel()
        logInResponse()
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        if (sharedPreferencesHelper.getEnterState(false)) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
        binding.loginActivityEnterButton.setOnClickListener {
            val email =binding.loginActivityUserEmail.text.toString()
            val password = binding.loginActivityUserPassword.text.toString()

            logIn(email,password)
        }
    }
    private fun logIn(email:String,password:String){
        log("$email $password")
        if (email.isNotEmpty() && password.isNotEmpty()) {
            binding.loginActivityProgressBar.visibility = View.VISIBLE
            myViewModel.logInUser(
                Login.LoginRequest(
                    email = email,
                    password = password
                )
            )
        } else {
            Toast.makeText(this,
                getString(R.string.login_activity_fill_all_fields),
                Toast.LENGTH_LONG).show()
        }
    }

    private fun logInResponse(){
        myViewModel.logInResponse.observe(this,{
            binding.loginActivityProgressBar.visibility = View.GONE
            if (it.payload !=null && it.code == 200){

                sharedPreferencesHelper.saveEnterState(true)
                sharedPreferencesHelper.saveAccessToken(it.payload!!.token!!)

                intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }else{
                Toast.makeText(this,"код ${it.code}",Toast.LENGTH_SHORT).show()
                log("$it access token is null")
            }
            log("$it logInResponse")
        })

        myViewModel.errorLogIn.observe(this,{
            binding.loginActivityProgressBar.visibility = View.GONE
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
            log("$it errorLogIn")
        })
    }

    private fun setUpViewModel() {
        myViewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(RetrofitBuilder().getApiInterface(this))
            ).get(
                MyViewModel::class.java
            )
    }
}