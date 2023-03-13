package com.volie.newsapphilt.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.volie.newsapphilt.databinding.FragmentBreakingNewsBinding
import com.volie.newsapphilt.util.Status
import com.volie.newsapphilt.view.fragment.adapter.NewsAdapter
import com.volie.newsapphilt.viewmodel.BreakingNewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BreakingNewsFragment
@Inject constructor() : Fragment() {
    private var _mBinding: FragmentBreakingNewsBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: BreakingNewsViewModel by viewModels()
    private var pageNumber = 1
    var page = -1
    var isLoading = false

    private val mAdapter: NewsAdapter by lazy {
        NewsAdapter {
            val action =
                BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(it)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        mViewModel.getBreakingNews(pageNumber)
        pullToRefresh()
        initObserver()
    }

    private fun setupAdapter() {
        with(mBinding.rvBreakingNews) {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(scrollListener)
        }
    }

    var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val visibleItemCount = layoutManager.childCount
            val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
            val total = mAdapter.itemCount

            if (!isLoading) {
                if ((visibleItemCount + pastVisibleItem) >= total) {
                    pageNumber++
                    mViewModel.getBreakingNews(pageNumber)
                }
            }
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    fun pullToRefresh() {
        with(mBinding) {
            swipeRefreshLayout.setOnRefreshListener {
                rvBreakingNews.visibility = View.GONE
                paginationProgressBar.visibility = View.VISIBLE
                swipeRefreshLayout.isRefreshing = false
                mViewModel.getBreakingNews(1)
            }
        }
    }

    fun initObserver() {
        mViewModel.news.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    mBinding.rvBreakingNews.visibility = View.VISIBLE
                    it.data?.let { news ->
                        with(mBinding) {
                            tvError.visibility = View.GONE
                            paginationProgressBar.visibility = View.GONE
                        }
                        mAdapter.differ.submitList(news.articles)
                    }
                }
                Status.ERROR -> {
                    with(mBinding) {
                        tvError.visibility = View.VISIBLE
                        paginationProgressBar.visibility = View.GONE
                    }
                    Log.e("Error!", "${it.message}")
                }
                Status.LOADING -> {
                    mBinding.paginationProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}