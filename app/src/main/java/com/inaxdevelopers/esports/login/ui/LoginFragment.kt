package com.inaxdevelopers.esports.login.ui

import android.app.Activity
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.inaxdevelopers.esports.R
import com.inaxdevelopers.esports.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1001

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.loginUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Enter email and password", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.forgotPassword.setOnClickListener {
                findNavController().navigate(R.id.action_login_to_forget)
            }
            binding.sinupText.setOnClickListener {
                findNavController().navigate(R.id.action_login_to_register)

            }
            binding.btnGoogleSignIn.setOnClickListener {
                signIn()
            }
        }

        viewModel.loginStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                "success" -> {
                    Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_forget_to_login)
                }

                "not_verified" -> {
                    Toast.makeText(
                        requireContext(), "Email not verified. Check your inbox.", Toast.LENGTH_LONG
                    ).show()
                }

                "failed" -> {
                    Toast.makeText(
                        requireContext(),
                        "Login Failed. Check your credentials.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
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
                    Toast.makeText(
                        requireContext(), "Welcome ${account?.displayName}", Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_forget_to_login)
                } catch (e: ApiException) {
                    Toast.makeText(
                        requireContext(), "Sign-in failed: ${e.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
}