package com.volie.newsapphilt.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.volie.newsapphilt.databinding.FragmentSearchNewsBinding
import com.volie.newsapphilt.view.fragment.adapter.NewsAdapter
import javax.inject.Inject

class SearchNewsFragment
@Inject constructor() : Fragment() {
    private var _mBinding: FragmentSearchNewsBinding? = null
    private val mBinding get() = _mBinding!!
    private val mAdapter: NewsAdapter by lazy {
        NewsAdapter {
            val action = SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(it)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _mBinding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}