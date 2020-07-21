package com.tootiyesolutions.acturdc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.tootiyesolutions.acturdc.R
import com.tootiyesolutions.acturdc.databinding.ItemNewsPreviewBinding
import com.tootiyesolutions.acturdc.model.Article
import com.tootiyesolutions.acturdc.ui.fragments.BreakingNewsFragmentDirections
import com.tootiyesolutions.acturdc.ui.fragments.SavedNewsFragmentDirections
import com.tootiyesolutions.acturdc.ui.fragments.SearchNewsFragmentDirections

class NewsViewHolder(private val binding: ItemNewsPreviewBinding) : RecyclerView.ViewHolder(binding.root), NewsClickListener{

    override fun onArticleClicked(v: View, article: Article) {
        try {
            val action =
                BreakingNewsFragmentDirections.actionNavigationBreakingNewsToDetailNewsFragment(article)
            Navigation.findNavController(v).navigate(action)
        }catch (t: Throwable){}

        try {
            val action =
                SavedNewsFragmentDirections.actionNavigationSavedNewsToDetailNewsFragment(article)
            Navigation.findNavController(v).navigate(action)
        }catch (t: Throwable){}

        try {
            val action =
                SearchNewsFragmentDirections.actionNavigationSearchNewsToDetailNewsFragment(article)
            Navigation.findNavController(v).navigate(action)
        }catch (t: Throwable){}
    }

    fun bind(article: Article?) {
        binding.article = article
        binding.listener = this
    }

    companion object {
        fun create(parent: ViewGroup): NewsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_news_preview, parent, false)
            return NewsViewHolder(ItemNewsPreviewBinding.bind(view))
        }
    }
}