package com.hakancevik.newsappbihaber.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.hakancevik.newsappbihaber.R
import com.hakancevik.newsappbihaber.databinding.FragmentArticleBinding
import com.hakancevik.newsappbihaber.databinding.FragmentBreakingNewsBinding
import com.hakancevik.newsappbihaber.util.customToast
import com.hakancevik.newsappbihaber.viewmodel.NewsViewModel


class ArticleFragment : Fragment() {


    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: NewsViewModel

    private val args: ArticleFragmentArgs by navArgs()

    private var routeKey = -1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[NewsViewModel::class.java]

        val article = args.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }



        binding.fab.setOnClickListener {

            viewModel.saveArticle(article)
            activity?.customToast("Successfully saved.")

        }

//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                findNavController().popBackStack()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(callback)


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

//        routeKey = args.routeKey
//
//        if (routeKey != -1) {
//            findNavController().clearBackStack(routeKey)
//        }


    }
}