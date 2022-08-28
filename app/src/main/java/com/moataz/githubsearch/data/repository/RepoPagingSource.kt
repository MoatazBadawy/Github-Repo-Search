package com.moataz.githubsearch.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.moataz.githubsearch.data.api.SearchAPIService
import com.moataz.githubsearch.data.model.Item
import retrofit2.HttpException
import java.io.IOException

private const val START_PAGE_INDEX = 1

class RepoPagingSource(
    private val searchApi: SearchAPIService,
    private val query: String
) : PagingSource<Int, Item>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val position = params.key ?: START_PAGE_INDEX
        return try {
            val apiResponse =
                searchApi.getSearchResponse(query, position, params.loadSize)
            val repos = apiResponse.items

            LoadResult.Page(
                data = repos,
                prevKey = if (position == START_PAGE_INDEX) null else position - 1,
                nextKey = if (repos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}