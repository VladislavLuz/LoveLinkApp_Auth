package com.example.lovelink.authorization

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.lovelink.R
import com.example.lovelink.authorization.connection.AuthorizationAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthorizationModel {

    fun sendRequest(server_ip: String):AuthorizationAPI{
        val authRetrofitPhone = Retrofit.Builder()
            .baseUrl(server_ip)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var authAPI = authRetrofitPhone.create(AuthorizationAPI::class.java)
        return authAPI
    }

    fun checkFieldCharacters(field: String, numberOfCharacters:Int):Boolean{
        return field.chars().allMatch {Character.isDigit(it)}&&field.length==numberOfCharacters
    }





}