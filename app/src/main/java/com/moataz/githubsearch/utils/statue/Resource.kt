package com.moataz.githubsearch.utils.statue

sealed class Resource<out T> {
    data class Success<T>(val data: T?) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    object Empty : Resource<Nothing>()
}