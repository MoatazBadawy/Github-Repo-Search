package com.moataz.githubsearch.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.moataz.githubsearch.data.repository.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest

class SearchViewModel : ViewModel() {
    private val repository = SearchRepository()
    private val query = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    var results = query.flatMapLatest {
        repository.getSearchResult(it)
    }.cachedIn(viewModelScope)

    fun search(query: String) {
        this.query.value = query
    }

}