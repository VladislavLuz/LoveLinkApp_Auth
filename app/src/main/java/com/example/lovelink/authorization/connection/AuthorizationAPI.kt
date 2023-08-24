package com.example.lovelink.authorization.connection

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthorizationAPI {
    @Headers("X-API-Key:76067a4a-38d2-40dd-b012-99a147e27a11")
    @POST("/api/v1/authorization/send-code")
        suspend fun sendPhone(@Body authPhoneRequest: AuthPhoneRequest):Response<AuthPhoneResponse>

    @Headers("X-API-Key:76067a4a-38d2-40dd-b012-99a147e27a11")
    @POST("/api/v1/authorization/verify-code")
        suspend fun sendCode(@Body authCodeRequest: AuthCodeRequest):Response<AuthPhoneResponse>
}