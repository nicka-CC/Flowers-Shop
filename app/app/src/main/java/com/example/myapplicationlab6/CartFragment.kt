package com.example.myapplicationlab6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_cart, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = root.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.cart_recycler)
        val checkoutBtn = root.findViewById<Button>(R.id.checkout_button)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val api = RetrofitClient.createApiService(requireContext())

        lifecycleScope.launch {
            try {
                val r = api.getCart()
                if (r.isSuccessful) {
                    val body = r.body()
                    val items = body?.items ?: emptyList()
                    // show product name and quantity using a quick adapter
                    recycler.adapter = object : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
                        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
                            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
                            return object : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {}
                        }

                        override fun getItemCount(): Int = items.size

                        override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
                            val item = items[position]
                            val tvName = holder.itemView.findViewById<TextView>(R.id.product_name)
                            val tvPrice = holder.itemView.findViewById<TextView>(R.id.product_price)
                            val iv = holder.itemView.findViewById<ImageView>(R.id.product_image)
                            tvName.text = item.product?.name ?: "—"
                            tvPrice.text = item.quantity?.toString() ?: "1"
                            Glide.with(iv.context).load(item.product?.image).placeholder(R.drawable.ic_placeholder_avatar).into(iv)
                        }
                    }
                }
            } catch (e: Exception) { e.printStackTrace() }
        }

        checkoutBtn.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val r = api.getCart()
                    if (r.isSuccessful) {
                        val currentCart = r.body()
                        val items = currentCart?.items ?: emptyList()
                        if (items.isEmpty()) {
                            Toast.makeText(requireContext(), "Корзина пуста", Toast.LENGTH_SHORT).show()
                            return@launch
                        }
                        val orderItems = items.mapNotNull { it.product?.id?.let { pId -> CartItemRequest(pId, it.quantity ?: 1) } }
                        val orderReq = CreateOrderRequest(address = "Адрес от мобильного", date_delivery = "", total_service = "0", items = orderItems)
                        val res = api.createOrder(orderReq)
                        if (res.isSuccessful) {
                            Toast.makeText(requireContext(), "Заказ создан", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Ошибка создания заказа", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) { e.printStackTrace(); Toast.makeText(requireContext(), "Ошибка", Toast.LENGTH_SHORT).show() }
            }
        }
    }
}
