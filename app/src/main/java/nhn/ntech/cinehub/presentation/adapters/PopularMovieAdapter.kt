package nhn.ntech.cinehub.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.data.constant.ConstantApi

class PopularMovieAdapter : RecyclerView.Adapter<PopularMovieAdapter.PopularMovieViewHolder>() {

    private val imgList: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PopularMovieAdapter.PopularMovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.popular_movie_img_item, parent, false)
        return PopularMovieAdapter.PopularMovieViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PopularMovieAdapter.PopularMovieViewHolder,
        position: Int,
    ) {
        Glide.with(holder.itemView.context)
            .load(ConstantApi.BASE_URL_IMAGE + imgList[position])
            .apply(RequestOptions.centerCropTransform())
            .into(holder.image)
    }

    override fun getItemCount(): Int = imgList.size

    fun setData(imgList: List<String>){
        this.imgList.clear()
        this.imgList.addAll(imgList)
        notifyDataSetChanged()
    }

    class PopularMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.image)
    }
}