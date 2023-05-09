package com.hakancevik.newsappbihaber.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.hakancevik.newsappbihaber.R
import com.hakancevik.newsappbihaber.adapter.NewsAdapter
import com.hakancevik.newsappbihaber.databinding.FragmentBreakingNewsBinding
import com.hakancevik.newsappbihaber.databinding.FragmentSavedNewsBinding
import com.hakancevik.newsappbihaber.databinding.FragmentSearchNewsBinding
import com.hakancevik.newsappbihaber.viewmodel.NewsViewModel
import javax.inject.Inject

class SearchNewsFragment @Inject constructor(
    val newsAdapter: NewsAdapter
) : Fragment() {

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!


    lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(NewsViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}