package com.tootiyesolutions.acturdc.adapter

import android.view.View
import com.tootiyesolutions.acturdc.model.Article

interface NewsClickListener {
    fun onArticleClicked(v: View, article: Article)
}