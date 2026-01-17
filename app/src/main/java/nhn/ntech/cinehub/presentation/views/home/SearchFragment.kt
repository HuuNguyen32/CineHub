package nhn.ntech.cinehub.presentation.views.home

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.data.model.movies.Result
import nhn.ntech.cinehub.data.repository.SearchViewModel
import nhn.ntech.cinehub.databinding.FragmentSearchBinding
import nhn.ntech.cinehub.presentation.adapters.GenreItemDecoration
import nhn.ntech.cinehub.presentation.adapters.GridSpacingItemDecoration
import nhn.ntech.cinehub.presentation.adapters.LastSearchAdapter
import nhn.ntech.cinehub.presentation.adapters.TopRateMovieAdapter
import nhn.ntech.cinehub.utils.OnItemMovieListener

@AndroidEntryPoint
class SearchFragment : Fragment(), OnItemMovieListener {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var movieAdapter: TopRateMovieAdapter
    private lateinit var lastSearchAdapter: LastSearchAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemSpacing = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._16sdp)

        movieAdapter = TopRateMovieAdapter(this)
        binding.rvMovieList.adapter = movieAdapter
        binding.rvMovieList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMovieList.addItemDecoration(GridSpacingItemDecoration(2, itemSpacing, true))
        binding.rvMovieList.setHasFixedSize(true)

        lastSearchAdapter = LastSearchAdapter(this)
        binding.rvLastSearch.adapter = lastSearchAdapter
        binding.rvLastSearch.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false)

        binding.edtSearch.addTextChangedListener { editable ->
            val query = editable.toString().trim()
            if (query.isNotEmpty()) {
                searchViewModel.searchMovies(query) // realtime search
            } else {
                searchViewModel.clearMovies()
                binding.rvMovieList.visibility = View.GONE
                binding.emptyState.visibility = View.GONE
                binding.constraintLastSearch.visibility = View.VISIBLE
            }
        }

        binding.edtSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {

                val query = binding.edtSearch.text.toString().trim()
                if (query.isNotEmpty()) {
                    searchViewModel.saveSearchHistory(query) // chỉ lưu khi Enter
                }
                true
            } else {
                false
            }
        }

        // Quan sát kết quả tìm kiếm
        searchViewModel.movies.observe(viewLifecycleOwner) { movies ->
            val query = binding.edtSearch.text.toString().trim()
            if (query.isEmpty()) {
                // Luôn reset UI khi ô search trống
                binding.rvMovieList.visibility = View.GONE
                binding.emptyState.visibility = View.GONE
                binding.constraintLastSearch.visibility = View.VISIBLE
                return@observe
            }
            if (movies.isNullOrEmpty()) {
                binding.rvMovieList.visibility = View.GONE
                binding.emptyState.visibility = View.VISIBLE
                binding.constraintLastSearch.visibility = View.GONE
            } else {
                binding.constraintLastSearch.visibility = View.GONE
                binding.rvMovieList.visibility = View.VISIBLE
                binding.emptyState.visibility = View.GONE
                movieAdapter.setData(movies)
            }
        }

        // Quan sát lịch sử tìm kiếm
        searchViewModel.lastSearch.observe(viewLifecycleOwner) { history ->
            lastSearchAdapter.setData(history)
        }

        // Xử lý nút Clear All
        binding.txtClearAll.setOnClickListener {
            searchViewModel.clearHistory()
        }

    }

    override fun onResume() {
        super.onResume()
        if (binding.edtSearch.text.isNullOrEmpty()) {
            searchViewModel.clearMovies()
            searchViewModel.clearHistory()
            binding.rvMovieList.visibility = View.GONE
            binding.emptyState.visibility = View.GONE
            binding.constraintLastSearch.visibility = View.VISIBLE
        }

    }

    override fun <T> onItemClick(item: T) {
        if (item is String){
            if (item == "") return
            searchViewModel.searchMovies(item)
            binding.edtSearch.setText(item)
        }

        if (item is Result){
            val action = SearchFragmentDirections.actionSearchFragmentToDetailMovieFragment(item.id)
            findNavController().navigate(action)
        }
    }


}