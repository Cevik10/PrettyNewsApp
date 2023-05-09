package com.hakancevik.newsappbihaber.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager

import com.hakancevik.newsappbihaber.adapter.NewsAdapter

import com.hakancevik.newsappbihaber.databinding.FragmentBreakingNewsBinding
import com.hakancevik.newsappbihaber.util.Resource
import com.hakancevik.newsappbihaber.util.customToast
import com.hakancevik.newsappbihaber.util.hide
import com.hakancevik.newsappbihaber.util.show
import com.hakancevik.newsappbihaber.viewmodel.NewsViewModel
import javax.inject.Inject


class BreakingNewsFragment @Inject constructor(
    val newsAdapter: NewsAdapter
) : Fragment() {

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: NewsViewModel

    private val TAG = "BreakingNewsFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(NewsViewModel::class.java)
        subscribeToObservers()

        binding.recyclerViewBreakingNews.adapter = newsAdapter
        binding.recyclerViewBreakingNews.layoutManager = LinearLayoutManager(requireContext())


    }

    private fun subscribeToObservers() {
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Success -> {
                    binding.paginationProgressBar.hide()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }

                is Resource.Error -> {
                    binding.paginationProgressBar.hide()
                    response.message?.let { message ->
                        Log.d(TAG, "error: $message")
                        requireActivity().customToast("$message")
                    }

                }

                is Resource.Loading -> {
                    binding.paginationProgressBar.show()


                }
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}