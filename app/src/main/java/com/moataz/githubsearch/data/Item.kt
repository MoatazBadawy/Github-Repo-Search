package com.moataz.githubsearch.data

data class Item(
    val id: Int,
    val full_name: String,
    val description: String,
    val html_url: String,
    val stargazers_count: Int,
    val created_at: String,
)