package com.moataz.githubsearch.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.moataz.githubsearch.data.repository.SearchRepository

 feature/paging_library
    private val repository = SearchRepository()

    private val state: SavedStateHandle = SavedStateHandle()
    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    val searchResponse = currentQuery.switchMap { queryString ->
        repository.getSearchResult(queryString).cachedIn(viewModelScope)
    }

    fun search(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private val DEFAULT_QUERY: String? = null

    private var searchText = ""
    private val searchRepository = SearchRepository()
    private val searchResponse: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()

    private fun search() {
        viewModelScope.launch {
            val response = searchRepository.getSearchResult(searchText, 1)
            response.collect { result ->
                searchResponse.postValue(result)
            }
        }
    }

    fun getSearchResponse(searchText: String): LiveData<Resource<SearchResponse>> {
        this.searchText = searchText
        search()
        return searchResponse
 develop
    }
}