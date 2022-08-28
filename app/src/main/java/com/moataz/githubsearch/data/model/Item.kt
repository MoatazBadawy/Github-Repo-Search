package com.moataz.githubsearch.data.model

data class Item(
    val id: String,
    val full_name: String,
    val description: String,
    val html_url: String,
    val stargazers_count: String,
    val created_at: String,
)