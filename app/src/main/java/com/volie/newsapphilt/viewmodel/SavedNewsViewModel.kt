package com.volie.newsapphilt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volie.newsapphilt.model.Article
import com.volie.newsapphilt.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedNewsViewModel
@Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun undoDelete(article: Article) {
        viewModelScope.launch {
            repository.insertArticle(article)
        }
    }

    fun getNewsFromLocal() = repository.getNewsFromLocal()

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            repository.deleteArticle(article)
        }
    }

    fun deleteAllSaved() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllSaved()
        }
    }
}