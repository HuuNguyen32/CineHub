package nhn.ntech.cinehub.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.data.constant.ConstantApi
import nhn.ntech.cinehub.data.model.movies.Result
import nhn.ntech.cinehub.utils.GenreMapper

class PopularAdapter : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    private val popularList: MutableList<Result> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PopularAdapter.PopularViewHolder {
        return PopularViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.popular_movie_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PopularAdapter.PopularViewHolder, position: Int) {
        val item = popularList[position]
        holder.txtTitle.text = item.title
        holder.txtGenre.text = item.genreIds.toString()
        holder.txtRate.text = item.voteAverage.toString()
        Glide.with(holder.itemView.context)
            .load(ConstantApi.BASE_URL_IMAGE + item.posterPath)
            .centerCrop()
            .into(holder.imgPoster)
    }

    override fun getItemCount(): Int = popularList.size


    fun setData(popularList: List<Result>){
        this.popularList.clear()
        this.popularList.addAll(popularList)
        notifyDataSetChanged()
    }


    class PopularViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPoster = itemView.findViewById<ImageView>(R.id.imgPoster)
        val txtTitle = itemView.findViewById<TextView>(R.id.txtTitle)
        val txtGenre = itemView.findViewById<TextView>(R.id.txtGenre)
        val txtRate = itemView.findViewById<TextView>(R.id.txtRate)
        val imgHeart = itemView.findViewById<ImageView>(R.id.imgHeart)
    }
}