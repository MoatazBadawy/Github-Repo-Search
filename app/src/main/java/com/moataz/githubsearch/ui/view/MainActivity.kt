package com.moataz.githubsearch.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moataz.githubsearch.data.model.Item
import com.moataz.githubsearch.databinding.ActivityMainBinding
import com.moataz.githubsearch.ui.adapter.SearchAdapter
import com.moataz.githubsearch.ui.viewmodel.SearchViewModel
import com.moataz.githubsearch.utils.statue.Resource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: SearchViewModel by viewModels()
    private val adapter = SearchAdapter()

    private var currentPage = 1
    private var newList: MutableList<Item> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        getSearchResult()
        pagingSearchResultInRecyclerview()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initAdapter() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.setOnTouchListener { _, motionEvent ->
            binding.recyclerView.onTouchEvent(motionEvent)
            true
        }
    }

    private fun getSearchResult() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getSearchResponse(query!!, currentPage).observe(this@MainActivity) {
                    when (it) {
                        is Resource.Loading -> {
                            binding.apply {
                                progressBar.visibility = View.VISIBLE
                                mainImage.visibility = View.GONE
                                welcomeSearchText.visibility = View.GONE
                                errorSearchText.visibility = View.GONE
                            }
                        }
                        is Resource.Success -> {
                            binding.apply {
                                progressBar.visibility = View.GONE
                            }
                            newList.addAll(it.data!!.items)
                            adapter.updateList(newList)
                        }
                        is Resource.Error -> {
                            binding.apply {
                                progressBar.visibility = View.GONE
                                mainImage.visibility = View.VISIBLE
                                welcomeSearchText.visibility = View.GONE
                                errorSearchText.visibility = View.VISIBLE
                            }
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

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

                if (lastVisibleItemPosition == totalItemCount - 1) {
                    currentPage++
                    viewModel.getSearchResponse(
                        binding.searchView.query.toString(), currentPage
                    )
                        .observe(this@MainActivity) {
                            when (it) {
                                is Resource.Loading -> {
                                    binding.apply {
                                        loadMoreProgressBar.visibility = View.VISIBLE
                                    }
                                }
                                is Resource.Success -> {
                                    binding.apply {
                                        loadMoreProgressBar.visibility = View.GONE
                                    }
                                    newList.addAll(it.data!!.items)
                                    adapter.updateList(newList)
                                }
                                is Resource.Error -> {
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
}