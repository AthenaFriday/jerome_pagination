package com.android.pagingdemo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.pagingdemo.data.Article
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun ArticleListScreen(viewModel: ArticleViewModel) {
    val articles = viewModel.items.collectAsLazyPagingItems()
    val isRefreshing = articles.loadState.refresh is LoadState.Loading
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Paging Basics") },
                backgroundColor = MaterialTheme.colors.primary
            )
        }
    ) { innerPadding ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { articles.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(articles.itemSnapshotList.items) { _, article ->
                    if (article != null) {
                        ArticleItem(article)
                        Divider()
                    }
                }

                if (articles.loadState.append is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                if (articles.loadState.append is LoadState.Error) {
                    val error = (articles.loadState.append as LoadState.Error).error
                    item {
                        RetrySection(
                            message = "Error loading more: ${error.localizedMessage ?: "Unknown"}",
                            onRetry = { articles.retry() }
                        )
                    }
                }

                if (articles.loadState.refresh is LoadState.Error) {
                    val error = (articles.loadState.refresh as LoadState.Error).error
                    item {
                        RetrySection(
                            message = "Initial load error: ${error.localizedMessage ?: "Unknown"}",
                            onRetry = { articles.retry() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleItem(article: Article) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = article.title,
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = article.description,
            style = MaterialTheme.typography.body2
        )
        Text(
            text = article.created,
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun RetrySection(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = MaterialTheme.colors.error)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
