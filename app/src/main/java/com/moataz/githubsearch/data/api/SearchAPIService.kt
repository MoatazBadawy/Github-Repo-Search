package com.moataz.githubsearch.data.api

import com.moataz.githubsearch.data.model.SearchResponse
import com.moataz.githubsearch.utils.statue.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAPIService {
    @GET("search/repositories")
    suspend fun getSearchResponse(
        @Query("q") query: String, @Query("page") page: Int = 1, @Query("per_page") perPage: Int = 15
    ): SearchResponse
}