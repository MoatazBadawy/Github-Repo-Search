package com.moataz.githubsearch.data.api

import com.moataz.githubsearch.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAPIService {
    @GET("search/repositories")
    suspend fun getSearchResponse(
        @Query("q") query: String, @Query("page") page: Int, @Query("per_page") perPage: Int
    ): SearchResponse
}