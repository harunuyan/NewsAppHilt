package com.volie.newsapphilt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volie.newsapphilt.model.News
import com.volie.newsapphilt.repo.Repository
import com.volie.newsapphilt.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel
@Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _searchNews = MutableLiveData<Resource<News>>()
    val searchNews: LiveData<Resource<News>> = _searchNews

    fun searchNews(searchQuery: String) {
        viewModelScope.launch {
            val result = repository.searchNews(searchQuery)
            _searchNews.postValue(result)
        }
    }
}