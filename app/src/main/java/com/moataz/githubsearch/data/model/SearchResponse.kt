package com.moataz.githubsearch.data.model

import androidx.annotation.Keep

@Keep
data class SearchResponse(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)