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
class BreakingNewsViewModel
@Inject constructor(
    val repository: Repository
) : ViewModel() {

    private val _news = MutableLiveData<Resource<News>>()
    val news: LiveData<Resource<News>> = _news

    fun getBreakingNews() {
        viewModelScope.launch {
            val response = repository.getBrekingNewsFromRemote()
            _news.postValue(response)
        }
    }
}