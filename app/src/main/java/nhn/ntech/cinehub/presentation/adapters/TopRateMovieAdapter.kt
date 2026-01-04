package nhn.ntech.cinehub.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.data.constant.ConstantApi
import nhn.ntech.cinehub.data.model.movies.Result
import nhn.ntech.cinehub.utils.OnItemMovieListener

class TopRateMovieAdapter(
    private val listener: OnItemMovieListener
) : RecyclerView.Adapter<TopRateMovieAdapter.TopRateMovieViewHolder>() {

    private val topRateMovies: MutableList<Result> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TopRateMovieAdapter.TopRateMovieViewHolder {
        return TopRateMovieAdapter.TopRateMovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.top_rate_item, parent, false))
    }

    override fun onBindViewHolder(
        holder: TopRateMovieAdapter.TopRateMovieViewHolder,
        position: Int,
    ) {
        val item = topRateMovies[position]
        holder.txtTitle.text = item.title
        holder.txtReleaseDate.text = item.releaseDate
        holder.txtLanguage.text = item.originalLanguage
        holder.txtRate.text = item.voteAverage.toString()
        Glide.with(holder.itemView.context)
            .load(ConstantApi.BASE_URL_IMAGE + item.posterPath)
            .centerCrop()
            .into(holder.imgPoster)

        holder.itemView.setOnClickListener {
            listener.onItemClick(item = item)
        }
    }

    override fun getItemCount(): Int = topRateMovies.size

    fun setData(topRateMovies: List<Result>){
        this.topRateMovies.clear()
        this.topRateMovies.addAll(topRateMovies)
        notifyDataSetChanged()
    }

    class TopRateMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       val imgPoster = itemView.findViewById<ImageView>(R.id.imgPoster)
        val txtTitle = itemView.findViewById<TextView>(R.id.txtTitle)
        val txtReleaseDate = itemView.findViewById<TextView>(R.id.txtReleaseDate)
        val txtLanguage = itemView.findViewById<TextView>(R.id.txtLanguage)
        val txtRate = itemView.findViewById<TextView>(R.id.txtRate)
    }
}