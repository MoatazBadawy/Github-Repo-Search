package com.moataz.githubsearch.data.request

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.moataz.githubsearch.data.api.SearchAPIService
import com.moataz.githubsearch.utils.helper.HttpRoutes.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    private fun myHttpClient(): OkHttpClient {

        val builder = OkHttpClient()
            .newBuilder()
            .callTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
        return builder.build()
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(myHttpClient())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()


    val searchApi: SearchAPIService = retrofit.create(SearchAPIService::class.java)
}