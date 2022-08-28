package com.moataz.githubsearch.data.repository

import com.moataz.githubsearch.data.model.SearchResponse
import com.moataz.githubsearch.data.request.ApiClient
import com.moataz.githubsearch.utils.statue.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchRepository {

    suspend fun getSearchResult(query: String, page: Int): Flow<Resource<SearchResponse>> = flow {
        try {
            val response = ApiClient.searchApi.getSearchResponse(query, page)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)
}