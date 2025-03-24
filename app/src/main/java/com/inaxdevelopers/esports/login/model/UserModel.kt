package com.inaxdevelopers.esports.login.model

data class UserModel(
    val userId: String = "",
    val gameId: String = "",
    val gameName: String = "",
    val email: String = "",
    val password: String = "",
    val phone: String = "",
    val loginType: String = ""  // "Google" or "Custom"
)
