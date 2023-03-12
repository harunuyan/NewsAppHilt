package com.volie.newsapphilt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volie.newsapphilt.model.Article
import com.volie.newsapphilt.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel
@Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun insertArticle(article: Article) {
        viewModelScope.launch {
            repository.insertArticle(article)
        }
    }
}