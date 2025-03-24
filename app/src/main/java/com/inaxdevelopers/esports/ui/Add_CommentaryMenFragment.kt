package com.inaxdevelopers.esports.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inaxdevelopers.esports.R

class Add_CommentaryMenFragment : Fragment() {

    companion object {
        fun newInstance() = Add_CommentaryMenFragment()
    }

    private val viewModel: AddCommentaryMenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add__commentary_men, container, false)
    }
}