package com.moataz.githubsearch.data.model

import androidx.annotation.Keep

@Keep
data class Item(
    val id: Int?,
    val full_name: String?,
    val description: String?,
    val html_url: String?,
    val stargazers_count: String?,
    val created_at: String?,
)