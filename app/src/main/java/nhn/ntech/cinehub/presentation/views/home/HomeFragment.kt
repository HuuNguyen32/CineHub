package nhn.ntech.cinehub.presentation.views.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.databinding.FragmentHomeBinding
import nhn.ntech.cinehub.presentation.adapters.GenreAdapter
import nhn.ntech.cinehub.presentation.adapters.GenreItemDecoration
import nhn.ntech.cinehub.presentation.adapters.HorizontalMarginItemDecoration
import nhn.ntech.cinehub.presentation.adapters.PopularAdapter
import nhn.ntech.cinehub.presentation.adapters.PopularMovieAdapter
import nhn.ntech.cinehub.presentation.adapters.TopRateMovieAdapter
import nhn.ntech.cinehub.presentation.viewmodels.MovieViewModel
import nhn.ntech.cinehub.utils.GenreMapper
import java.util.Collections.emptyList

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var popularMovieAdapter: PopularMovieAdapter
    private lateinit var popularAdapter: PopularAdapter
    private lateinit var genreAdapter: GenreAdapter
    private lateinit var topRateMovieAdapter: TopRateMovieAdapter
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPopular()
        initPopular()
        setGenre()
        initGenres()
        setTopRated()
        initTopRated()
    }

    private fun setTopRated() {
        val sidePadding = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._16sdp)
        val itemSpacing = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._12sdp)
        topRateMovieAdapter = TopRateMovieAdapter()
        binding.rvMovies.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvMovies.adapter = topRateMovieAdapter
        binding.rvMovies.addItemDecoration(GenreItemDecoration(sidePadding = sidePadding, itemSpacing = itemSpacing))
    }

    private fun setPopular() {
        val sidePadding = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._16sdp)
        val itemSpacing = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._12sdp)
        // set carousel
        popularMovieAdapter = PopularMovieAdapter()
        binding.vpPopularMovie.adapter = popularMovieAdapter
        setupCarousel()
        val dotsIndicator = binding.dotIndicator
        dotsIndicator.attachTo(binding.vpPopularMovie)

        // set Popular
        popularAdapter = PopularAdapter()
        binding.rvRecommend.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvRecommend.adapter = popularAdapter
    }

    private fun setGenre() {
        val sidePadding = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._16sdp)
        genreAdapter = GenreAdapter()
        binding.rvGenres.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvGenres.adapter = genreAdapter
        binding.rvGenres.addItemDecoration(GenreItemDecoration(sidePadding, null))
    }

    private fun initPopular(){
        movieViewModel.getPopularMovies()
        movieViewModel.popularMovies.observe(viewLifecycleOwner){
            response ->
            val popularMovies = response.data?.results ?: emptyList()
            if (popularMovies.isNotEmpty()){
                binding.popularProgressBar.visibility = View.GONE
            }
            val popularMovieImgList = popularMovies.map { it.backdropPath }.take(5).toMutableList()
            popularMovieAdapter.setData(popularMovieImgList)
            popularAdapter.setData(popularMovies)
            binding.vpPopularMovie.setCurrentItem(1, false)
        }
    }

    private fun initGenres(){
        movieViewModel.getGenres()
        movieViewModel.genres.observe(viewLifecycleOwner){
            response ->
            val genres = response.data?.genres ?: emptyList()
            if (genres.isNotEmpty()){
                binding.genreProgressBar.visibility = View.GONE
            }
            val genresList = genres.map { it.name }.toMutableList()
            genresList.add(0, getString(R.string.all))
            genreAdapter.setData(genresList)
        }
    }

    private fun initTopRated(){
        movieViewModel.getTopRatedMovies()
        movieViewModel.topRatedMovies.observe(viewLifecycleOwner){
            response ->
            val topRates = response.data?.results ?: emptyList()
            if (topRates != null){
                binding.movieProgressBar.visibility = View.GONE
            }
            topRateMovieAdapter.setData(topRates)
        }
    }




    private fun setupCarousel(){

        binding.vpPopularMovie.offscreenPageLimit = 1

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1f - (0.25f * kotlin.math.abs(position)) // kích thước
            page.alpha = 0.25f + (1 - kotlin.math.abs(position)) // độ mờ
        }
        binding.vpPopularMovie.setPageTransformer(pageTransformer)

        val itemDecoration = HorizontalMarginItemDecoration(
            binding.root.context,
            R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.vpPopularMovie.addItemDecoration(itemDecoration)
    }


}