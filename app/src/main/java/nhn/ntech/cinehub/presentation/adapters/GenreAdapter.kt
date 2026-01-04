package nhn.ntech.cinehub.presentation.adapters

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.utils.OnItemMovieListener

class GenreAdapter(val listener: OnItemMovieListener) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    private val genres: MutableList<String> = mutableListOf()
    private var selectedPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): GenreAdapter.GenreViewHolder {
        return GenreAdapter.GenreViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.genre_item, parent, false))
    }

    override fun onBindViewHolder(holder: GenreAdapter.GenreViewHolder, position: Int) {
        holder.txtGenreName.text = genres[position]

        val isCurrentSelected = (holder.bindingAdapterPosition == selectedPosition)
        val colorTextSelected = ContextCompat.getColor(holder.itemView.context, R.color.white)
        val colorTextUnselected = ContextCompat.getColor(holder.itemView.context, R.color.gray_50)

        holder.itemView.isSelected = isCurrentSelected

        if (isCurrentSelected){
            holder.txtGenreName.setTextColor(colorTextSelected)
            holder.txtGenreName.setTypeface(null, Typeface.BOLD)
        }else{
            holder.txtGenreName.setTextColor(colorTextUnselected)
            holder.txtGenreName.setTypeface(null, Typeface.NORMAL)
        }

        holder.itemView.setOnClickListener {
           if (selectedPosition != holder.bindingAdapterPosition ){
               val oldSelectedPosition = selectedPosition
               selectedPosition = holder.bindingAdapterPosition
               notifyItemChanged(oldSelectedPosition)
               notifyItemChanged(selectedPosition)
               listener.onItemClick(genres[selectedPosition])
           }

        }
    }

    override fun getItemCount(): Int = genres.size

    fun setData(genres: List<String>){
        this.genres.clear()
        this.genres.addAll(genres)
        notifyDataSetChanged()
    }

    class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtGenreName = itemView.findViewById<TextView>(R.id.tvGenreName)
    }
}