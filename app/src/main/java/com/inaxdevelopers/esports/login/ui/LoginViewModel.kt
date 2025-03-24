package com.inaxdevelopers.esports.login.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> get() = _loginStatus

    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        _loginStatus.value = "success"
                    } else {
                        _loginStatus.value = "not_verified"
                    }
                } else {
                    _loginStatus.value = "failed"
                }
            }
    }

}