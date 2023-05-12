package com.hakancevik.newsappbihaber.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hakancevik.newsappbihaber.R
import com.hakancevik.newsappbihaber.adapter.NewsAdapter
import com.hakancevik.newsappbihaber.databinding.FragmentBreakingNewsBinding
import com.hakancevik.newsappbihaber.databinding.FragmentSavedNewsBinding
import com.hakancevik.newsappbihaber.databinding.FragmentSearchNewsBinding
import com.hakancevik.newsappbihaber.util.Resource
import com.hakancevik.newsappbihaber.util.customToast
import com.hakancevik.newsappbihaber.util.hide
import com.hakancevik.newsappbihaber.util.show
import com.hakancevik.newsappbihaber.viewmodel.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchNewsFragment @Inject constructor(
    private val newsAdapter: NewsAdapter
) : Fragment() {

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!

    private val TAG = "SearchNewsFragment"
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

        viewModel = ViewModelProvider(requireActivity())[NewsViewModel::class.java]


        binding.recyclerViewSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            newsAdapter.differ.submitList(arrayListOf())
        }


        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }

            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle)
        }


        var job: Job? = null
        binding.searchViewEditText.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(600L)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Success -> {
                    binding.paginationProgressBar.hide()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                        Log.d("system.out", newsResponse.articles.toString())
                    }
                }

                is Resource.Error -> {
                    binding.paginationProgressBar.hide()
                    response.message?.let { message ->
                        Log.d(TAG, "error: $message")
                        requireActivity().customToast(message)
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