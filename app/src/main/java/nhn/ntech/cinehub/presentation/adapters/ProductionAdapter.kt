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
import nhn.ntech.cinehub.data.model.details.ProductionCompany

class ProductionAdapter : RecyclerView.Adapter<ProductionAdapter.ProductionViewHolder>() {

    private val productions: MutableList<ProductionCompany> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ProductionAdapter.ProductionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.production_item, parent, false)
        return ProductionAdapter.ProductionViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductionAdapter.ProductionViewHolder,
        position: Int,
    ) {
        val item = productions[position]
        holder.txtName.text = item.name
        Glide.with(holder.itemView.context)
            .load(ConstantApi.BASE_URL_IMAGE + item.logoPath)
            .placeholder(R.drawable.ic_default)
            .centerCrop()
            .into(holder.imgLogo)
    }

    override fun getItemCount(): Int = productions.size

    fun setData(productions: List<ProductionCompany>){
        this.productions.clear()
        this.productions.addAll(productions)
        notifyDataSetChanged()
    }


    class ProductionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imgLogo = itemView.findViewById<ImageView>(R.id.imgLogo)
        val txtName = itemView.findViewById<TextView>(R.id.txtNameProduction)
    }
}