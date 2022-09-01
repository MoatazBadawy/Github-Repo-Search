package com.moataz.githubsearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.moataz.githubsearch.R
import com.moataz.githubsearch.data.model.Item
import com.moataz.githubsearch.databinding.ItemRepoBinding

class SearchAdapter :
    PagingDataAdapter<Item, SearchAdapter.SearchViewHolder>(RepoSearchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {

        return SearchViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_repo,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val search = getItem(position)
        if (search != null) {
            holder.itemRepoBinding.repoModel = search
        }
    }

    inner class SearchViewHolder(var itemRepoBinding: ItemRepoBinding) :
        RecyclerView.ViewHolder(
            itemRepoBinding.root
        )
}

class RepoSearchDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.full_name == newItem.full_name
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}