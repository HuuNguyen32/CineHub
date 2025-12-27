package nhn.ntech.cinehub.presentation.views.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.databinding.FragmentHomeBinding
import nhn.ntech.cinehub.presentation.adapters.HorizontalMarginItemDecoration
import nhn.ntech.cinehub.presentation.adapters.PopularMovieAdapter
import nhn.ntech.cinehub.presentation.viewmodels.MovieViewModel
import java.util.Collections.emptyList

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: PopularMovieAdapter
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
//        val listImg = mutableListOf(R.drawable.movie1, R.drawable.movie2, R.drawable.movie3, R.drawable.movie4, R.drawable.movie5)
        adapter = PopularMovieAdapter(emptyList())
        binding.vpPopularMovie.adapter = adapter
        binding.vpPopularMovie.setCurrentItem(1, false)
        setupCarousel()
        val dotsIndicator = binding.dotIndicator
        dotsIndicator.attachTo(binding.vpPopularMovie)
        initData()

    }

    private fun initData(){
        movieViewModel.getPopularMovies()
        movieViewModel.popularMovies.observe(viewLifecycleOwner){
            response ->
            val popularMovies = response.data?.results ?: emptyList()
            val popularMovieImgList = popularMovies.map { it.backdropPath }.toMutableList()
            adapter.setData(popularMovieImgList)
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