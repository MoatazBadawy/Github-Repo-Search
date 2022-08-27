package com.moataz.githubsearch.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.moataz.githubsearch.R
import com.moataz.githubsearch.data.model.SearchResponse
import com.moataz.githubsearch.databinding.ItemRepoBinding

class SearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: SearchResponse? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: SearchResponse?) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_repo,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val search = items!!.items[position]
        (holder as SearchViewHolder).itemRepoBinding.repoModel = search
    }

    override fun getItemCount(): Int {
        return if (items == null) 0 else items!!.items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearDataFromAdappter() {
        items = null
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(var itemRepoBinding: ItemRepoBinding) :
        RecyclerView.ViewHolder(
            itemRepoBinding.root
        )
}