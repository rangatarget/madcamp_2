package com.example.madcamp_2

import java.io.Serializable

data class RegisterModel(
    var id: String,
    var password: String,
    var classes: String
)


data class RegisterResult(
    var message: Boolean
)

data class LoginModel(
    var id: String,
    var pw: String
)

data class LoginResult(
    var token : String,
    var id : String
)

data class IdCertification(
    var id : String
)

data class IdCertificationResult(
    var isExist : Boolean
)
