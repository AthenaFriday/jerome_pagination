package com.android.pagingdemo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.android.pagingdemo.data.Article
import com.android.pagingdemo.data.ArticleRepository
import kotlinx.coroutines.flow.Flow

private const val ITEMS_PER_PAGE = 50

class ArticleViewModel : ViewModel() {
    private val repository = ArticleRepository()

    val items: Flow<PagingData<Article>> = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
        pagingSourceFactory = { repository.articlePagingSource() }
    ).flow.cachedIn(viewModelScope)
}
