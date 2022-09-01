package com.moataz.githubsearch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.moataz.githubsearch.data.model.Item
import com.moataz.githubsearch.data.model.SearchResponse
import com.moataz.githubsearch.data.request.ApiClient
import kotlinx.coroutines.flow.Flow

class SearchRepository {
    private val apiClient = ApiClient.searchApi

    fun getSearchResult(query: String) = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { RepoPagingSource(apiClient, query) }
    ).flow
}