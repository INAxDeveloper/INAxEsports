package com.inaxdevelopers.esports.login.model

data class User(
    val uid: String? = "",
    val displayName: String? = "",
    val email: String? = "",
    val phoneNumber: String? = "",
    val profilePic: String = "",
    val isVerified: Boolean = false

)
