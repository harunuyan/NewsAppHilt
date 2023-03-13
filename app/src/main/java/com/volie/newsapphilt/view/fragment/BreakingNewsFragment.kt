package com.volie.newsapphilt.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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

@AndroidEntryPoint
class BreakingNewsFragment : Fragment() {
    private var _mBinding: FragmentBreakingNewsBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: BreakingNewsViewModel by viewModels()
    private var pageNumber = 1
    var isLoading = false
    var countryCode = "1"

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
        mViewModel.getBreakingNews(1, countryCode)
        selectedSpinnerItem()
        pullToRefresh()
        initObserver()
    }

    private fun selectedSpinnerItem() {
        mBinding.spCountries.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                countryCode =
                    mViewModel.parseCountry(position.toString())
                mViewModel.getBreakingNews(pageNumber, countryCode)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                countryCode = mViewModel.parseCountry("0")
            }
        }
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
                    mViewModel.getBreakingNews(pageNumber, countryCode)
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
                mViewModel.getBreakingNews(1, countryCode)
            }
        }
    }

    fun initObserver() {
        mViewModel.news.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { news ->
                        with(mBinding) {
                            tvError.visibility = View.GONE
                            paginationProgressBar.visibility = View.GONE
                            rvBreakingNews.visibility = View.VISIBLE
                        }
                        // operator overloading -> +
                        mAdapter.differ.submitList(mAdapter.differ.currentList + news.articles)
                    }
                    isLoading = false
                }
                Status.ERROR -> {
                    with(mBinding) {
                        rvBreakingNews.visibility = View.GONE
                        tvError.visibility = View.VISIBLE
                        paginationProgressBar.visibility = View.GONE
                    }
                    Log.e("Error!", "${it.message}")
                    isLoading = false
                }
                Status.LOADING -> {
                    with(mBinding) {
                        paginationProgressBar.visibility = View.VISIBLE
                        tvError.visibility = View.GONE
                        rvBreakingNews.visibility = View.GONE
                    }

                    isLoading = true
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}