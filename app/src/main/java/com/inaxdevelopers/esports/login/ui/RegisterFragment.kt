package com.inaxdevelopers.esports.login.ui

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.inaxdevelopers.esports.MainActivity
import com.inaxdevelopers.esports.R
import com.inaxdevelopers.esports.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val authViewModel: RegisterViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1001


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        binding.btnGoogleRegister.setOnClickListener {
//            signIn()
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 1001)

        }
        binding.btnRegister.setOnClickListener {
            val gameId = binding.etgameID.text.toString().trim()
            val gameName = binding.etgameName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.registerUser(gameId, gameName, email, password, phone)
            } else {
                Toast.makeText(requireContext(), "Fill all required fields", Toast.LENGTH_SHORT).show()
            }
        }
        observeViewModel()
        observeLoginStatus()
        observeUserVerification()
        observeEmailVerification()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.result
            account?.idToken?.let { authViewModel.googleLogin(it) }
        }
    }

    private fun observeViewModel() {
        authViewModel.registrationResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Registration Failed", Toast.LENGTH_SHORT).show()
            }
        }

        authViewModel.googleLoginResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Google Sign-In Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeEmailVerification() {
        authViewModel.isEmailVerified.observe(viewLifecycleOwner) { isVerified ->
            if (!isVerified) {
                showVerificationDialog()
            }
        }
    }

    private fun showVerificationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Verify Your Email")
            .setMessage("Please verify your email address to continue.")
            .setPositiveButton("Send Email") { _, _ ->
                authViewModel.sendVerificationEmail()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun signIn() {
        googleSignInLauncher.launch(googleSignInClient.signInIntent)
    }

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    authViewModel.signInWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Toast.makeText(
                        requireContext(),
                        "Sign-in failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    private fun observeLoginStatus() {
        authViewModel.loginStatus.observe(viewLifecycleOwner) { (success, message) ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            if (success) {
                val destination = ActivityNavigator(requireContext()).createDestination().setIntent(
                    Intent(requireContext(), MainActivity::class.java)
                )
                val navController = findNavController()
                navController.navigate(destination.id)

            }
        }
    }

    private fun observeUserVerification() {
        authViewModel.isUserVerified.observe(viewLifecycleOwner) { isVerified ->
            if (!isVerified) {
                Toast.makeText(
                    requireContext(),
                    "Please wait for admin verification",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}