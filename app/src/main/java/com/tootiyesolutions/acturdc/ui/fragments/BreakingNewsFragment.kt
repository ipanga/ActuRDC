package com.tootiyesolutions.acturdc.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.tootiyesolutions.acturdc.Injection
import com.tootiyesolutions.acturdc.adapter.NewsAdapter
import com.tootiyesolutions.acturdc.adapter.NewsLoadStateAdapter
import com.tootiyesolutions.acturdc.databinding.FragmentBreakingNewsBinding
import com.tootiyesolutions.acturdc.ui.SearchNewsViewModel
import com.tootiyesolutions.acturdc.util.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class BreakingNewsFragment : Fragment() {

    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SearchNewsViewModel
    private val adapter = NewsAdapter()

    private var searchJob: Job? = null

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        val view = binding.root

        // get the view model
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(requireContext()))
            .get(SearchNewsViewModel::class.java)

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.rvBreakingNews.addItemDecoration(decoration)

        initAdapter()
        val query = savedInstanceState?.getString(Constants.LAST_SEARCH_QUERY) ?: Constants.GET_BREAKING_NEWS
        search(query)
        binding.btRetryBreakingNews.setOnClickListener { adapter.retry() }
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.LAST_SEARCH_QUERY, Constants.GET_BREAKING_NEWS)
    }

    private fun initAdapter() {
        binding.rvBreakingNews.adapter = adapter.withLoadStateHeaderAndFooter(
            header = NewsLoadStateAdapter { adapter.retry() },
            footer = NewsLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.rvBreakingNews.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.pbBreakingNews.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.btRetryBreakingNews.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    context,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}