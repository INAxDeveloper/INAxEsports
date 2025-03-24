package com.inaxdevelopers.esports.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.inaxdevelopers.esports.login.model.User

class ProfileViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("Users")

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun getUserProfile() {
        val uid = auth.currentUser?.uid ?: return
        database.child(uid).get().addOnSuccessListener { snapshot ->
            _user.value = snapshot.getValue(User::class.java)
        }.addOnFailureListener {
            _user.value = null
        }
    }

    fun logout() {
        auth.signOut()
    }
}

