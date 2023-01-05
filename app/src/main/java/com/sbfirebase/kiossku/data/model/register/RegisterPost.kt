package com.sbfirebase.kiossku.data.model.register

data class RegisterPost(
    val fullname : String,
    val telepon : String,
    val email : String,
    val password : String,
    val confirm_password : String
)
