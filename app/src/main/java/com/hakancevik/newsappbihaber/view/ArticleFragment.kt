package com.hakancevik.newsappbihaber.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.hakancevik.newsappbihaber.R
import com.hakancevik.newsappbihaber.databinding.FragmentArticleBinding
import com.hakancevik.newsappbihaber.util.customToast
import com.hakancevik.newsappbihaber.viewmodel.NewsViewModel

import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hakancevik.newsappbihaber.util.gone
import com.hakancevik.newsappbihaber.util.hide


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
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = (requireActivity() as AppCompatActivity).findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.mToolbar)
        toolbar?.let {
            (requireActivity() as AppCompatActivity).setSupportActionBar(it)
            (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

            args.article.title?.let { title ->
                it.title = title
            }

            val backButton = it.navigationIcon
            backButton?.setTint(Color.WHITE)
            backButton?.mutate()?.setBounds(0, 0, 50, 50)
            it.navigationIcon = backButton

            it.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }


        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.gone()




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