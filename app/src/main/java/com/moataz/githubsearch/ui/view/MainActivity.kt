package com.moataz.githubsearch.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.moataz.githubsearch.databinding.ActivityMainBinding
import com.moataz.githubsearch.ui.adapter.SearchAdapter
import com.moataz.githubsearch.ui.viewmodel.SearchViewModel
import com.moataz.githubsearch.utils.statue.Resource

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: SearchViewModel by viewModels()
    private val adapter = SearchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        getSearchResult()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initAdapter() {
        binding.recyclerView.setOnTouchListener { _, motionEvent ->
            binding.recyclerView.onTouchEvent(motionEvent)
            true
        }
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun getSearchResult() {
        binding.searchView.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getSearchResponse(query!!).observe(this@MainActivity) {
                    when (it) {
                        is Resource.Loading -> {
                            binding.searchView.clearFocus()
                            adapter.clearDataFromAdappter()
                            binding.progressBar.visibility = View.VISIBLE
                            binding.searchStart.visibility = View.GONE
                            binding.textViecw.visibility = View.GONE
                            binding.errorText.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.searchView.clearFocus()
                            binding.progressBar.visibility = View.GONE
                            binding.searchStart.visibility = View.GONE
                            binding.textViecw.visibility = View.GONE
                            binding.errorText.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                            adapter.setData(it.data)
                        }
                        is Resource.Error -> {
                            binding.searchView.clearFocus()
                            adapter.clearDataFromAdappter()
                            binding.progressBar.visibility = View.GONE
                            binding.searchStart.visibility = View.VISIBLE
                            binding.errorText.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
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
}