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
    var nickname : String,
    var image : String,
    var id : String,
    var classes: String
)

data class IdCertification(
    var id : String
)

data class IdCertificationResult(
    var isExist : Boolean
)

data class kakaoExist(
    var id: String
)

data class kakaologinreturn(
    var id: String,
    val nickname: String,
    val image: String,
    val classes: String
)

data class kakaoregister(
    var id: String,
    var profile: String,
    var classes: String,
    var nickname: String
)

data class changepassword(
    var id: String,
    var oldpw: String,
    var newpw: String
)

data class UrlResult(
    var url: String
)

data class signout(
    var id: String
)