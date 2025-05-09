package com.android.pagingdemo.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

private const val STARTING_KEY = 0
private const val LOAD_DELAY_MILLIS = 3000L
private const val MILLIS_PER_DAY = 86_400_000L // 24 * 60 * 60 * 1000

class ArticlePagingSource : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val startKey = params.key ?: STARTING_KEY
        val range = startKey until (startKey + params.loadSize)

        // Simulate network delay
        delay(LOAD_DELAY_MILLIS)

        val currentTime = System.currentTimeMillis()
        val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        val articles = range.map { id ->
            Article(
                id = id,
                title = "Article $id",
                description = "This describes article $id",
                created = dateFormatter.format(Date(currentTime - id * MILLIS_PER_DAY))
            )
        }

        return LoadResult.Page(
            data = articles,
            prevKey = if (startKey == STARTING_KEY) null else ensureValidKey(startKey - params.loadSize),
            nextKey = range.last + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val closestItem = state.closestItemToPosition(anchorPosition) ?: return null
        return ensureValidKey(closestItem.id - (state.config.pageSize / 2))
    }

    private fun ensureValidKey(key: Int): Int = max(STARTING_KEY, key)
}
