package nhn.ntech.cinehub.presentation.views.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.data.model.genre.Genre
import nhn.ntech.cinehub.data.model.movies.Result
import nhn.ntech.cinehub.databinding.FragmentMovieBinding
import nhn.ntech.cinehub.presentation.adapters.GenreAdapter
import nhn.ntech.cinehub.presentation.adapters.GenreItemDecoration
import nhn.ntech.cinehub.presentation.adapters.GridSpacingItemDecoration
import nhn.ntech.cinehub.presentation.adapters.TopRateMovieAdapter
import nhn.ntech.cinehub.presentation.viewmodels.AuthViewModel
import nhn.ntech.cinehub.presentation.viewmodels.MovieViewModel
import nhn.ntech.cinehub.presentation.viewmodels.UserViewModel
import nhn.ntech.cinehub.utils.OnItemMovieListener
import java.util.Collections

@AndroidEntryPoint
class MovieFragment : Fragment(), OnItemMovieListener {

    private lateinit var binding: FragmentMovieBinding
    private lateinit var adapter: TopRateMovieAdapter
    private lateinit var genreAdapter: GenreAdapter
    private val movieArgs: MovieFragmentArgs by navArgs()
    private lateinit var message: String
    private val movieViewModel: MovieViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private var genreList: List<Genre> = Collections.emptyList()
    private var movieList: List<Result> = Collections.emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        message = movieArgs.message
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        if (authViewModel.isLoggedIn()){

            val itemSpacing = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._16sdp)

            setAdapters(itemSpacing)

            initGenres()

            when(message){
                "TOP_RATE" -> {
                    binding.txtTitleHeader.text = "Top Rate Movies"
                    initTopRated()
                }
                "RECOMMEND" -> {
                    binding.txtTitleHeader.text = "Recommend Movies"
                    initPopular()
                }
                "FAVORITE" -> {
                    binding.btnMore.visibility = View.VISIBLE
                    binding.txtTitleHeader.text = "Favorite Movies"
                    val uid = userViewModel.currentUid.toString()
                    initFavorite(uid)
                }
            }
        }

    }

    private fun initFavorite(uid: String) {
        userViewModel.observeFavorites(uid)
        userViewModel.favoriteMovies.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()){
                movieList = movieViewModel.convertFactory(list, genreList)
                adapter.setData(movieList)
            }
        }
    }

    private fun setAdapters(itemSpacing: Int) {
        // Common Adapter
        adapter = TopRateMovieAdapter(this)
        binding.rvMovieList.adapter = adapter
        binding.rvMovieList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMovieList.addItemDecoration(GridSpacingItemDecoration(2, itemSpacing, true))
        binding.rvMovieList.setHasFixedSize(true)

        // GenreAdapter
        genreAdapter = GenreAdapter(this)
        binding.rvGenres.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvGenres.adapter = genreAdapter
        binding.rvGenres.addItemDecoration(GenreItemDecoration(itemSpacing, null))
    }

    private fun initTopRated(){
        movieViewModel.getTopRatedMovies()
        movieViewModel.topRatedMovies.observe(viewLifecycleOwner){
                response ->
            val topRates = response.data?.results ?: Collections.emptyList()
            if (topRates.isNotEmpty()){
                movieList = movieViewModel.convertFactory(topRates, genreList)
                adapter.setData(movieList)
            }
        }
    }


    private fun initPopular() {
        movieViewModel.getPopularMovies()
        movieViewModel.popularMovies.observe(viewLifecycleOwner) { response ->
            val popularMovies = response.data?.results ?: Collections.emptyList()
            if (popularMovies.isNotEmpty()) {
                movieList = movieViewModel.convertFactory(popularMovies, genreList)
                adapter.setData(movieList)
            }
        }
    }

    private fun initGenres(){
        movieViewModel.getGenres()
        movieViewModel.genres.observe(viewLifecycleOwner){
                response ->
            val genres = response.data?.genres ?: Collections.emptyList()
            if (genres.isNotEmpty()){
                genreList = genres
                val genresList = genres.map { it.name }.toMutableList()
                genresList.add(0, getString(R.string.all))
                genreAdapter.setData(genresList)
            }
        }
    }

    override fun <T> onItemClick(item: T) {
        if (item is String){
            if (item == "All"){
                adapter.setData(movieList)
            }else{
                val filterTopRates = if (movieList.isNotEmpty()){
                    movieList.filter { it.genreNames.contains(item) }
                }else Collections.emptyList()
                adapter.setData(filterTopRates)
            }
        }
        if (item is Result){
            val action = MovieFragmentDirections.actionMovieFragmentToDetailMovieFragment(item.id)
            findNavController().navigate(action)
        }
    }

}