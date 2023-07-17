package com.example.lovelink.authorization.connection

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthorizationAPI {
@POST("api/v1/reg/send_code")
    suspend fun sendPhone(@Body authPhoneRequest: AuthPhoneRequest):Response<AuthPhoneResponse>
}