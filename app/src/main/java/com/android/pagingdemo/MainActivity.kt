package com.android.pagingdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.pagingdemo.ui.ArticleListScreen
import com.android.pagingdemo.ui.ArticleViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val viewModel: ArticleViewModel = viewModel()
                ArticleListScreen(viewModel)
            }
        }
    }
}
