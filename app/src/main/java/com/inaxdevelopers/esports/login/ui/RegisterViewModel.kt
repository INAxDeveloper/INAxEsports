package com.inaxdevelopers.esports.login.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.inaxdevelopers.esports.login.model.User
import com.inaxdevelopers.esports.login.model.UserModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterViewModel : ViewModel() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _registrationResult = MutableLiveData<Boolean>()
    val registrationResult: LiveData<Boolean> get() = _registrationResult

    private val _googleLoginResult = MutableLiveData<Boolean>()
    val googleLoginResult: LiveData<Boolean> get() = _googleLoginResult

    fun registerUser(
        gameId: String,
        gameName: String,
        email: String,
        password: String,
        phone: String
    ) {
        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = result.user?.uid ?: return@launch

                val user = UserModel(userId, gameId, gameName, email, password, phone, "Custom")
                db.collection("users").document(userId).set(user).await()

                _registrationResult.postValue(true)
            } catch (e: Exception) {
                _registrationResult.postValue(false)
            }
        }
    }

    fun googleLogin(idToken: String) {
        viewModelScope.launch {
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = auth.signInWithCredential(credential).await()
                val userId = result.user?.uid ?: return@launch

                val user = UserModel(
                    userId,
                    gameId = "",
                    gameName = "",
                    email = result.user?.email ?: "",
                    password = "",
                    phone = result.user?.phoneNumber ?: "",
                    loginType = "Google"
                )
                db.collection("users").document(userId).set(user).await()

                _googleLoginResult.postValue(true)
            } catch (e: Exception) {
                _googleLoginResult.postValue(false)
            }
        }
    }



private val _loginStatus = MutableLiveData<Pair<Boolean, String>>()
val loginStatus: LiveData<Pair<Boolean, String>> get() = _loginStatus

private val _isUserVerified = MutableLiveData<Boolean>()
val isUserVerified: LiveData<Boolean> get() = _isUserVerified

private val _isEmailVerified = MutableLiveData<Boolean>()
val isEmailVerified: LiveData<Boolean> get() = _isEmailVerified


fun signInWithGoogle(idToken: String) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    checkUserExists(it.uid) { exists ->
                        if (exists) {
                            checkUserVerification(it.uid)
                        } else {
                            saveUserToDatabase(it)
                        }
                    }
                }
            } else {
                _loginStatus.postValue(Pair(false, "Login Failed"))
            }
        }
}

private fun checkUserExists(uid: String, callback: (Boolean) -> Unit) {
    database.child(uid).get().addOnSuccessListener {
        callback(it.exists())
    }.addOnFailureListener {
        callback(false)
    }
}

private fun saveUserToDatabase(user: FirebaseUser) {
    val userData = User(
        uid = user.uid,
        displayName = user.displayName ?: "",
        email = user.email ?: "",
        phoneNumber = user.phoneNumber,
        profilePic = user.photoUrl.toString(),
        isVerified = user.isEmailVerified
    )
    database.child(user.uid).setValue(userData).addOnSuccessListener {
        _isEmailVerified.postValue(user.isEmailVerified)
        _loginStatus.postValue(Pair(true, "User Registered! Please verify your email."))
    }.addOnFailureListener {
        _loginStatus.postValue(Pair(false, "Failed to save user data"))
    }
}

private fun checkUserVerification(uid: String) {
    database.child(uid).get().addOnSuccessListener { snapshot ->
        val isVerified = snapshot.child("isVerified").getValue(Boolean::class.java) ?: false
        if (isVerified) {
            _isUserVerified.postValue(true)
            _loginStatus.postValue(Pair(true, "Login Successful!"))
        } else {
            _isUserVerified.postValue(false)
            _loginStatus.postValue(Pair(false, "Your account is not verified yet!"))
        }
    }
}

fun sendVerificationEmail() {
    auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
        _loginStatus.postValue(Pair(false, "Verification email sent! Check your inbox."))
    }?.addOnFailureListener {
        _loginStatus.postValue(Pair(false, "Failed to send verification email"))
    }
}

}