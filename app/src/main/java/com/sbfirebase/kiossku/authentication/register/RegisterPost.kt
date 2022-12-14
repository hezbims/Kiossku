package com.sbfirebase.kiossku.authentication.register

data class RegisterPost(
    val fullname : String,
    val telepon : String,
    val email : String,
    val password : String,
    val confirm_password : String
)
