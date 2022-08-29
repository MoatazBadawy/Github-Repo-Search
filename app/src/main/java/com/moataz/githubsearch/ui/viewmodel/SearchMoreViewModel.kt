package com.moataz.githubsearch.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moataz.githubsearch.data.model.SearchResponse
import com.moataz.githubsearch.data.repository.SearchRepository
import com.moataz.githubsearch.utils.statue.Resource
import kotlinx.coroutines.launch

class SearchMoreViewModel : ViewModel() {
    private var searchText = ""
    var currentPage = 1
    private val searchRepository = SearchRepository()
    private val searchResponse: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()

    private fun searchMore() {
        currentPage++
        viewModelScope.launch {
            val response = searchRepository.getSearchResult(searchText, currentPage)
            response.collect { result ->
                searchResponse.postValue(result)
            }
        }
    }

    fun getMoreSearchResponseResult(searchText: String, page: Int): LiveData<Resource<SearchResponse>> {
        this.searchText = searchText
        this.currentPage = page
        searchMore()
        return searchResponse
    }
}