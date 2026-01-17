package nhn.ntech.cinehub.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.utils.OnItemMovieListener

class LastSearchAdapter(
    private val listener: OnItemMovieListener
) : RecyclerView.Adapter<LastSearchAdapter.LastSearchViewHolder>() {

    private val lastSearchList = mutableListOf<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): LastSearchAdapter.LastSearchViewHolder {
        return LastSearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.last_search_item, parent, false))
    }

    override fun onBindViewHolder(
        holder: LastSearchAdapter.LastSearchViewHolder,
        position: Int,
    ) {
        val item = lastSearchList[position]
        holder.txtKeyword.text = item
        holder.imgDelete.setOnClickListener {
            removeItem(position)
        }
        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun getItemCount(): Int = lastSearchList.size

    fun setData(data: List<String>){
        lastSearchList.clear()
        lastSearchList.addAll(data)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        lastSearchList.removeAt(position)
        notifyItemRemoved(position)
    }

    class LastSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtKeyword = itemView.findViewById<TextView>(R.id.txtKeyword)
        val imgDelete = itemView.findViewById<ImageView>(R.id.imgDelete)
    }
}

