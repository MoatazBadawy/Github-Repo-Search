package com.moataz.githubsearch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.moataz.githubsearch.data.request.ApiClient

class SearchRepository {
    private val apiClient = ApiClient.searchApi

    fun getSearchResult(query: String?) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RepoPagingSource(apiClient, query) }
        ).liveData
}