package com.volie.newsapphilt.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.volie.newsapphilt.databinding.FragmentSavedNewsBinding
import com.volie.newsapphilt.view.fragment.adapter.NewsAdapter
import com.volie.newsapphilt.viewmodel.SavedNewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedNewsFragment
@Inject constructor() : Fragment() {
    private var _mBinding: FragmentSavedNewsBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: SavedNewsViewModel by viewModels()
    private val mAdapter: NewsAdapter by lazy {
        NewsAdapter {
            val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(
                ArticleFragmentArgs(it).article
            )
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _mBinding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        initObserver()
    }


    private fun setupRecyclerView() {
        with(mBinding) {
            rvSavedNews.adapter = mAdapter
            rvSavedNews.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initObserver() {
        mViewModel.getNewsFromLocal().observe(viewLifecycleOwner) {
            mAdapter.differ.submitList(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}