package com.moataz.githubsearch.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
 feature/paging_library
import com.moataz.githubsearch.databinding.ActivityMainBinding
import com.moataz.githubsearch.ui.adapter.SearchAdapter
import com.moataz.githubsearch.ui.adapter.SearchRepoStateAdapter

import androidx.recyclerview.widget.RecyclerView
import com.moataz.githubsearch.databinding.ActivityMainBinding
import com.moataz.githubsearch.ui.adapter.SearchAdapter
import com.moataz.githubsearch.ui.viewmodel.SearchMoreViewModel
 develop
import com.moataz.githubsearch.ui.viewmodel.SearchViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private val searchMoreViewModel: SearchMoreViewModel by viewModels()
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
                welcomeSearchText.isVisible = loadState.source.refresh !is LoadState.Loading

                // empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    errorSearchText.isVisible = true
                } else {
                    errorSearchText.isVisible = false
                }
            }
        }
    }

    private fun sendSearchQuery() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
 feature/paging_library
                query?.let {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.search(it)
                    binding.searchView.clearFocus()

                searchViewModel.getSearchResponse(query!!).observe(this@MainActivity) {
                    when (it) {
                        is Resource.Loading -> {
                            binding.searchView.clearFocus()
                            binding.apply {
                                progressBar.visibility = View.VISIBLE
                                mainImage.visibility = View.GONE
                                welcomeSearchText.visibility = View.GONE
                                errorSearchText.visibility = View.GONE
                            }
                        }
                        is Resource.Success -> {
                            binding.searchView.clearFocus()
                            binding.apply {
                                progressBar.visibility = View.GONE
                            }
                            adapter.setData(it.data?.items!!)
                        }
                        is Resource.Error -> {
                            binding.searchView.clearFocus()
                            binding.apply {
                                progressBar.visibility = View.GONE
                                welcomeSearchText.visibility = View.GONE
                                adapter.clearData()
                                mainImage.visibility = View.VISIBLE
                                errorSearchText.visibility = View.VISIBLE

                            }
                        }
                    }
 develop
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
 feature/paging_library


    private fun pagingSearchResultInRecyclerview() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val currentPage = searchMoreViewModel.currentPage

                if (lastVisibleItemPosition == totalItemCount - 1) {
                    searchMoreViewModel.getMoreSearchResponseResult(
                        binding.searchView.query.toString(), currentPage
                    )
                        .observe(this@MainActivity) {
                            when (it) {
                                is Resource.Loading -> {
                                    binding.searchView.clearFocus()
                                    binding.apply {
                                        loadMoreProgressBar.visibility = View.VISIBLE
                                    }
                                }
                                is Resource.Success -> {
                                    binding.searchView.clearFocus()
                                    binding.apply {
                                        loadMoreProgressBar.visibility = View.GONE
                                    }
                                    adapter.updateData(it.data?.items!!)
                                }
                                is Resource.Error -> {
                                    binding.searchView.clearFocus()
                                    binding.apply {
                                        loadMoreProgressBar.visibility = View.GONE
                                    }
                                }
                            }
                        }
                }
            }
        })
    }
 develop
}