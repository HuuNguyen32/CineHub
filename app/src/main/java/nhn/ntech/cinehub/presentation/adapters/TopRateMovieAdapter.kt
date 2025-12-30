package nhn.ntech.cinehub.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import nhn.ntech.cinehub.R

class TopRateMovieAdapter : RecyclerView.Adapter<TopRateMovieAdapter.TopRateMovieViewHolder>() {

    private val topRateMovies: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TopRateMovieAdapter.TopRateMovieViewHolder {
        return TopRateMovieAdapter.TopRateMovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false))
    }

    override fun onBindViewHolder(
        holder: TopRateMovieAdapter.TopRateMovieViewHolder,
        position: Int,
    ) {

    }

    override fun getItemCount(): Int = topRateMovies.size

    class TopRateMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.image)
    }
}