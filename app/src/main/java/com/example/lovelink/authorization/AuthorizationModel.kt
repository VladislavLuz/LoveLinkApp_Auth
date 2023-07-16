package com.example.lovelink.authorization

import com.example.lovelink.authorization.connection.AuthorizationAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthorizationModel {
    lateinit var authAPI: AuthorizationAPI

    suspend fun sendRequest(){


    }

    fun checkPhoneField(textPhoneField:String):Boolean{
        if(textPhoneField.length == 10){
            return true
        }
        return false
    }


}