package com.moataz.githubsearch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.moataz.githubsearch.data.model.Item
import com.moataz.githubsearch.data.repository.RepoPagingSource
import com.moataz.githubsearch.data.request.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class SearchViewModel : ViewModel() {
    private val apiClient = ApiClient.searchApi

    fun getSearchResult(query: String): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(
                pageSize = 35,
                maxSize = 60,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RepoPagingSource(apiClient, query) }
        ).flow.cachedIn(viewModelScope).flowOn(Dispatchers.IO)
    }
}