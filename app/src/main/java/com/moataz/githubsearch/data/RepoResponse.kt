package com.moataz.githubsearch.data

data class RepoResponse(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)