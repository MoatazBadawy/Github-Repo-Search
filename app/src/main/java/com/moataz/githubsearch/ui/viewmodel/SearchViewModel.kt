package com.moataz.githubsearch.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.moataz.githubsearch.data.repository.SearchRepository

class SearchViewModel : ViewModel() {
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
    }
}