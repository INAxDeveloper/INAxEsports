package com.inaxdevelopers.esports.login.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.inaxdevelopers.esports.MainActivity
import com.inaxdevelopers.esports.R

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserStatus()
        }, 2000)

    }


    private fun checkUserStatus() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user != null && user.isEmailVerified) {
            val destination = ActivityNavigator(requireContext()).createDestination()
                .setIntent(Intent(requireContext(), MainActivity::class.java))
            val navController = findNavController()
            navController.navigate(destination.id)
        } else {

            findNavController().navigate(R.id.action_splash_to_login)
        }
    }
}