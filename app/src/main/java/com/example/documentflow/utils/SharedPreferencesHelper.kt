package com.example.documentflow.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.documentflow.R


class SharedPreferencesHelper(context: Context) {
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
    companion object{
        const val ACCESS_TOKEN_KEY = "access_token"
        const val ENTER_STATE_KEY = "login_key"
    }



    /**
     * Сохраняем аксесс токен, чтобы по нему обращаться к защищенному ресурсу
     * ставится аксес токен в хэдер запроса каждый раз когда делается запрос,
     * при лог ауте очищается и рефреш и аксес токены.
     * */
    fun saveAccessToken(token: String){
        val editor = sharedPreferences.edit()
        editor.putString(ACCESS_TOKEN_KEY,token)
        editor.apply()

    }
    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY,null)
    }
    fun clearAccessToken() {
        sharedPreferences.edit().remove(ACCESS_TOKEN_KEY).apply()
    }

    /**
     * Сохраняем стэйт при авторизации в приложение и каждый раз при
     * открытии приложения будем проверять если уже авторизован
     * не показывать страницу авторизации*/
    fun getEnterState(value: Boolean): Boolean {
        return sharedPreferences.getBoolean(ENTER_STATE_KEY, value)
    }

    fun saveEnterState(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(ENTER_STATE_KEY, value)
        editor.apply()
    }

    fun clearEnterState() {
        sharedPreferences.edit().remove(ENTER_STATE_KEY).apply()
    }








}