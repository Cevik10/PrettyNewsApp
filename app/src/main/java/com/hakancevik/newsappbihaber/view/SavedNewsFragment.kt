package com.hakancevik.newsappbihaber.view

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.hakancevik.newsappbihaber.R
import com.hakancevik.newsappbihaber.adapter.NewsAdapter

import com.hakancevik.newsappbihaber.databinding.FragmentSavedNewsBinding
import com.hakancevik.newsappbihaber.util.hide
import com.hakancevik.newsappbihaber.util.show
import com.hakancevik.newsappbihaber.viewmodel.SavedNewsViewModel
import javax.inject.Inject

class SavedNewsFragment @Inject constructor(
    private val newsAdapter: NewsAdapter
) : Fragment() {

    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: SavedNewsViewModel

    private val swipeCallBack = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
    ) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val layoutPosition = viewHolder.layoutPosition
            val selectedArticle = newsAdapter.differ.currentList[layoutPosition]
            viewModel.deleteArticle(selectedArticle)

            val snackbar = Snackbar.make(requireView(), "Successfully deleted.", Snackbar.LENGTH_SHORT)

            val params = snackbar.view.layoutParams as CoordinatorLayout.LayoutParams
            params.anchorId = R.id.bottomNavigationView // Bottom Navigation Bar'ın ID'sini buraya girin
            params.anchorGravity = Gravity.TOP
            params.gravity = Gravity.TOP
            snackbar.view.layoutParams = params

            snackbar.setAction("undo") {
                viewModel.saveArticle(selectedArticle)
            }
            snackbar.show()


        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = (requireActivity() as AppCompatActivity).findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.mToolbar)
        toolbar?.let {
            (requireActivity() as AppCompatActivity).setSupportActionBar(it)
            (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
            it.title = "NewsApp"
        }

        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.show()


        viewModel = ViewModelProvider(requireActivity())[SavedNewsViewModel::class.java]


        binding.recyclerViewSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            newsAdapter.differ.submitList(arrayListOf())
        }

        ItemTouchHelper(swipeCallBack).attachToRecyclerView(binding.recyclerViewSavedNews)


        newsAdapter.setOnItemClickListener {
            val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(it)
            findNavController().navigate(action)
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner) { articles ->
            if (articles.isEmpty()) {
                viewModel.savedNewsInfo.value = true
                newsAdapter.differ.submitList(articles)
            } else {
                newsAdapter.differ.submitList(articles)
                viewModel.savedNewsInfo.value = false
            }
        }

        viewModel.savedNewsInfo.observe(viewLifecycleOwner) {
            if (it) {
                binding.recyclerViewSavedNews.hide()
                binding.savedNewsInfoLayout.show()
            } else {
                binding.recyclerViewSavedNews.show()
                binding.savedNewsInfoLayout.hide()
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}