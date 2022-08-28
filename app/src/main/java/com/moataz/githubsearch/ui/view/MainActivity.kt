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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        getSearchResult()
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
                if (query != null) {
                    viewModel.getSearchResult(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}