package com.example.myapplicationlab6

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(private val items: List<Product>, private val onClick: (Product) -> Unit) : RecyclerView.Adapter<ProductAdapter.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.product_image)
        val name: TextView = view.findViewById(R.id.product_name)
        val price: TextView = view.findViewById(R.id.product_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val product = items[position]
        holder.name.text = product.name ?: "—"
        holder.price.text = product.amount?.toString() ?: "—"
        Glide.with(holder.image.context)
            .load(product.image)
            .placeholder(R.drawable.ic_placeholder_avatar)
            .into(holder.image)

        holder.itemView.setOnClickListener { onClick(product) }
    }

    override fun getItemCount(): Int = items.size
}
