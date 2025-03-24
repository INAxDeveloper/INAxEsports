package com.inaxdevelopers.esports.login

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class Esports : Application() {

    override fun onCreate() {
        super.onCreate()
        auth = FirebaseAuth.getInstance()
    }

    companion object {
        lateinit var auth: FirebaseAuth
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")
    }
}