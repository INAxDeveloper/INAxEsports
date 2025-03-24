package com.inaxdevelopers.esports.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inaxdevelopers.esports.R

class TournamentDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = TournamentDetailsFragment()
    }

    private val viewModel: TournamentDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_tournament_details, container, false)
    }
}