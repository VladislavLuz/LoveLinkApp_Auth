package com.example.lovelink.authorization.connection

data class AuthCodeRequest(
    var phone:String,
    var code:String
)
