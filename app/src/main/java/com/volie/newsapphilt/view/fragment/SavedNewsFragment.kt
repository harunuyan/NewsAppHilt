package com.volie.newsapphilt.view.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.volie.newsapphilt.R
import com.volie.newsapphilt.databinding.FragmentSavedNewsBinding
import com.volie.newsapphilt.model.Article
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
    ): View {
        _mBinding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupMenu()
        initObserver()
        deleteArticle()
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.saved_fragment_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId) {
                        R.id.delete_all_saved -> confirmRemoval()
                    }
                    return true
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    fun restoreDeletedArticle(view: View, deletedItem: Article) {
        Snackbar.make(
            view,
            "Deleted succesfully : ${deletedItem.title}",
            Snackbar.LENGTH_SHORT
        ).apply {
            this.setAction("Undo") {
                mViewModel.undoDelete(deletedItem)
            }
            this.show()
        }
    }

    private fun deleteArticle() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedItem = mAdapter.differ.currentList[position]
                mViewModel.deleteArticle(deletedItem)
                restoreDeletedArticle(viewHolder.itemView, deletedItem)
            }

        }).attachToRecyclerView(mBinding.rvSavedNews)
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

    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        with(builder) {
            setMessage("Are you sure you want to remove everything?")
            setTitle("Delete Everything?")
            setNegativeButton("Cancel") { _, _ -> }
            setPositiveButton("Yes") { _, _ ->
                mViewModel.deleteAllSaved()
                Toast.makeText(requireContext(), "Removed Everything!", Toast.LENGTH_SHORT).show()
            }
            create().show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}