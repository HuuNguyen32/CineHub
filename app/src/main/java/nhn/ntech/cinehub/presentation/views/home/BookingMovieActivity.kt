package nhn.ntech.cinehub.presentation.views.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.data.constant.ConstantApi
import nhn.ntech.cinehub.data.model.details.DetailMovieResponse
import nhn.ntech.cinehub.databinding.ActivityBookingMovieBinding
import nhn.ntech.cinehub.presentation.viewmodels.MovieViewModel
import kotlin.getValue
import kotlin.properties.Delegates

@AndroidEntryPoint
class BookingMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingMovieBinding
    private val viewModel: MovieViewModel by viewModels()
    private var movieID by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookingMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setPadding()
        movieID = intent.getIntExtra("movieId", -1)
        if (movieID != -1){
            initData(movieID)
        }
        setEventClicks()
    }

    private fun setEventClicks() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnBook.setOnClickListener {
            // Xử lý đặt vé
        }
    }

    private fun setPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initData(movieId: Int){
        viewModel.getDetailMovie(movieId)
        viewModel.detailMovie.observe(this) { response ->
            val movie = response.data
            if (movie != null) {
                bindData(movie)
            }
        }
    }


    private fun bindData(movie: DetailMovieResponse){
        // Tiêu đề phim
        binding.tvTitle.text = movie.title

        Glide.with(this)
            .load(ConstantApi.BASE_URL_IMAGE + movie.posterPath)
            .placeholder(R.drawable.ic_launcher_background)
            .centerCrop()
            .into(binding.imgPoster)

        // Thông tin chi tiết: thể loại, thời lượng, ngày phát hành
        val genres = movie.genres.joinToString(", ") { it.name }
        val runtimeText = "${movie.runtime} mins"
        val releaseDate = movie.releaseDate
        binding.tvInfo.text = "$genres | $runtimeText | $releaseDate"

        // Giá vé giả định (ví dụ 100k / vé)
        val ticketPrice = 100000
        binding.totalPrice.text = "$ticketPrice VND"

    }
}