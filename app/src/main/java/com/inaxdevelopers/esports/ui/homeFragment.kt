package com.inaxdevelopers.esports.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.inaxdevelopers.esports.R
import com.inaxdevelopers.esports.databinding.FragmentHomeBinding

class homeFragment : Fragment() {

    companion object {
        fun newInstance() = homeFragment()
    }

    private lateinit var binding: FragmentHomeBinding

    private lateinit var viewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        return binding.root
    }
}