package com.volie.newsapphilt.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.volie.newsapphilt.databinding.FragmentSearchNewsBinding
import com.volie.newsapphilt.util.Status
import com.volie.newsapphilt.view.fragment.adapter.NewsAdapter
import com.volie.newsapphilt.viewmodel.SearchNewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchNewsFragment
@Inject constructor() : Fragment() {
    private var _mBinding: FragmentSearchNewsBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: SearchNewsViewModel by viewModels()
    private val mAdapter: NewsAdapter by lazy {
        NewsAdapter {
            val action = SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(
                ArticleFragmentArgs(it).article
            )
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        mBinding.etSearch.addTextChangedListener {
            it?.let {
                if (it.toString().isNotEmpty()) {
                    mViewModel.searchNews(it.toString())
                } else {
                    mAdapter.differ.submitList(emptyList())
                }
            }
        }
        initObserver()
    }

    private fun setupRecyclerView() {
        with(mBinding) {
            rvSearchNews.adapter = mAdapter
            rvSearchNews.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initObserver() {
        mViewModel.searchNews.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {

                Status.SUCCESS -> {
                    resource.data?.let {
                        with(mBinding) {
                            paginationProgressBar.visibility = View.GONE
                            tvError.visibility = View.GONE
                        }
                        mAdapter.differ.submitList(it.articles)
                    }
                }
                Status.ERROR -> {
                    with(mBinding) {
                        tvError.visibility = View.VISIBLE
                        paginationProgressBar.visibility = View.GONE
                        rvSearchNews.visibility = View.GONE
                        Log.e("Error!", "${resource.message}")
                    }
                }
                Status.LOADING -> {
                    mBinding.tvError.visibility = View.GONE
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