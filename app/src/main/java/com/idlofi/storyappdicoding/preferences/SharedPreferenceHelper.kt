package com.idlofi.storyappdicoding.preferences

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceHelper(context: Context) {
    val login = "login"
    val PREF_NAME = "main_pref"
    val KEY_TOKEN = "Bearer"

    val sharedPreference: SharedPreferences

    init {
        sharedPreference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setStatusLogin(status: Boolean){
        sharedPreference.edit().putBoolean(login, status).apply()
    }

    fun getStatusLogin(): Boolean{
        return sharedPreference.getBoolean(login,false)
    }

    fun saveUserToken(token: String){
        sharedPreference.edit().putString(KEY_TOKEN,token).apply()
    }

    fun getUserToken(): String? {
        return sharedPreference.getString(KEY_TOKEN," ")
    }

    fun clearUserToken(){
        sharedPreference.edit().remove(KEY_TOKEN).apply()
    }

    fun clearUserLogin(){
        sharedPreference.edit().remove(login).apply()
    }

}
