package nhn.ntech.cinehub.presentation.views.details

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.intuit.sdp.R
import dagger.hilt.android.AndroidEntryPoint
import nhn.ntech.cinehub.data.constant.ConstantApi
import nhn.ntech.cinehub.data.model.details.DetailMovieResponse
import nhn.ntech.cinehub.data.model.movies.Result
import nhn.ntech.cinehub.databinding.FragmentDetailMovieBinding
import nhn.ntech.cinehub.presentation.adapters.GenreItemDecoration
import nhn.ntech.cinehub.presentation.adapters.ProductionAdapter
import nhn.ntech.cinehub.presentation.adapters.TopRateMovieAdapter
import nhn.ntech.cinehub.presentation.viewmodels.MovieViewModel
import nhn.ntech.cinehub.presentation.views.home.HomeFragmentDirections
import nhn.ntech.cinehub.utils.OnItemMovieListener
import kotlin.properties.Delegates

@AndroidEntryPoint
class DetailMovieFragment : Fragment(), OnItemMovieListener {

    private lateinit var binding: FragmentDetailMovieBinding
    private lateinit var adapter: ProductionAdapter
    private lateinit var recommendAdapter: TopRateMovieAdapter
    private val viewModel: MovieViewModel by viewModels()
    private val args: DetailMovieFragmentArgs by navArgs()
    private var movieId by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieId = args.movieId
        setBack()
        initData()
        setMore()
    }

    private fun setMore() {
        binding.txtOverviewContent.setOnClickListener {
            if (binding.txtOverviewContent.ellipsize == null){
                binding.txtOverviewContent.maxLines = 3
                binding.txtOverviewContent.ellipsize = TextUtils.TruncateAt.END
            }else{
                binding.txtOverviewContent.maxLines = 10
                binding.txtOverviewContent.ellipsize = null
            }
        }
    }

    private fun setBack() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initData() {
        initDetailData()
        initRecommendData()
    }

    private fun initRecommendData() {
        setRecommendData()
        viewModel.getRecommendations(movieId)
        viewModel.recommendations.observe(viewLifecycleOwner) { response ->
            val movies = response.data?.results ?: emptyList()
            if (movies.isNotEmpty()){
                binding.recommendationProgressBar.visibility = View.GONE
                recommendAdapter.setData(movies)
            }
        }
    }

    private fun setRecommendData() {
        val sidePadding = resources.getDimensionPixelSize(R.dimen._16sdp)
        val itemSpacing = resources.getDimensionPixelSize(R.dimen._12sdp)
        recommendAdapter = TopRateMovieAdapter(this)
        binding.rvRecommendation.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvRecommendation.adapter = recommendAdapter
        if (binding.rvRecommendation.itemDecorationCount == 0) {
            binding.rvRecommendation.addItemDecoration(
                GenreItemDecoration(
                    sidePadding = sidePadding,
                    itemSpacing = itemSpacing
                )
            )
        }

    }

    private fun initDetailData() {
        viewModel.getDetailMovie(movieId)
        viewModel.detailMovie.observe(viewLifecycleOwner) { response ->
            val movie = response.data
            if (movie != null) {
                bindData(movie)
            }
        }
    }

    private fun bindData(movie: DetailMovieResponse) {
        val sidePadding = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._16sdp)
        val itemSpacing = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._12sdp)

        binding.txtTitle.text = movie.title
        binding.txtGenres.text = movie.genres.joinToString { it.name }
        binding.txtRate.text = movie.voteAverage.toString()
        binding.txtCount.text = "("+movie.voteCount.toString()+")"
        binding.txtDuration.text = convertMinuteToTime(movie.runtime)
        binding.txtLanguage.text = movie.spokenLanguages[0].name
        binding.txtOverviewContent.text = movie.overview

        Glide.with(this)
            .load(ConstantApi.BASE_URL_IMAGE + movie.posterPath)
            .centerCrop()
            .into(binding.imgPoster)

        setProductionData(movie, sidePadding, itemSpacing)
    }

    private fun setProductionData(
        movie: DetailMovieResponse,
        sidePadding: Int,
        itemSpacing: Int,
    ) {
        adapter = ProductionAdapter()
        binding.rvProductions.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvProductions.adapter = adapter
        binding.productionsProgressBar.visibility = View.GONE
        adapter.setData(movie.productionCompanies)
        if (binding.rvProductions.itemDecorationCount == 0) {
            binding.rvProductions.addItemDecoration(GenreItemDecoration(sidePadding, itemSpacing))
        }
    }

    private fun convertMinuteToTime(runtime: Int): String{
        val hours = runtime / 60
        val minutes = runtime % 60
        return "$hours hrs $minutes mins"
    }

    override fun <T> onItemClick(item: T) {
        if (item is Result){
            movieId = item.id
            initData() // gọi lại để load dữ liệu mới
        }
    }
}