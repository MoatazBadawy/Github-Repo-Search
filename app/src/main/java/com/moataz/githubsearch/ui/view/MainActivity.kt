package com.moataz.githubsearch.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.moataz.githubsearch.databinding.ActivityMainBinding
import com.moataz.githubsearch.ui.adapter.SearchAdapter
import com.moataz.githubsearch.ui.adapter.SearchRepoStateAdapter
import com.moataz.githubsearch.ui.viewmodel.SearchViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: SearchViewModel by viewModels()
    private val adapter = SearchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        sendSearchQuery()
        getListOfSearch()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initAdapter() {
        binding.recyclerView.setHasFixedSize(true)

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.setOnTouchListener { _, motionEvent ->
            binding.recyclerView.onTouchEvent(motionEvent)
            true
        }

        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = SearchRepoStateAdapter { adapter.retry() },
            footer = SearchRepoStateAdapter { adapter.retry() }
        )
    }

    private fun getListOfSearch() {
        viewModel.searchResponse.observe(this) {
            adapter.submitData(lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                mainImage.isVisible = loadState.source.refresh is LoadState.Error
                errorSearchText.isVisible = loadState.source.refresh is LoadState.Error

                // empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    errorSearchText.isVisible = true
                    mainImage.isVisible = true
                } else {
                    errorSearchText.isVisible = false
                }

                // welcome message
                if (adapter.itemCount > 1 || loadState.source.refresh is LoadState.Loading || loadState.source.refresh !is LoadState.Error) {
                    welcomeSearchText.visibility = View.GONE
                } else {
                    welcomeSearchText.visibility = View.VISIBLE
                }

            }
        }
    }

    private fun sendSearchQuery() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.search(it)
                    binding.searchView.clearFocus()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}