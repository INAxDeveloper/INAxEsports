package com.inaxdevelopers.esports.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.inaxdevelopers.esports.R
import com.inaxdevelopers.esports.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding


    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserProfile()
        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.tvName.text = user.displayName
                binding.tvEmail.text = user.email
                binding.tvStatus.text = if (user.isVerified) "Verified ✅" else "Not Verified ❌"

                Glide.with(this)
                    .load(user.profilePic)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(binding.ivProfileImage)
            }
        }

        binding.addTem.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_add_Team)
        }

        binding.addComtre.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_Add_CommentaryMenFragment)

        }

//        binding.btnLogout.setOnClickListener {
//            viewModel.logout()
//            findNavController().navigate(R.id.)
//        }
    }

}