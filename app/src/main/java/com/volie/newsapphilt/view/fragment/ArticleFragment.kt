package com.volie.newsapphilt.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.volie.newsapphilt.databinding.FragmentArticleBinding
import com.volie.newsapphilt.viewmodel.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArticleFragment
@Inject constructor() : Fragment() {
    private var _mBinding: FragmentArticleBinding? = null
    private val mBinding get() = _mBinding!!
    private val mViewModel: ArticleViewModel by viewModels()
    val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentArticleBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        mBinding.webView.apply {
            webViewClient = WebViewClient()
            try {
                loadUrl(article.url)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        mBinding.fab.setOnClickListener {
            mViewModel.insertArticle(article)
            Snackbar.make(view, "Article saved succesfully", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

}