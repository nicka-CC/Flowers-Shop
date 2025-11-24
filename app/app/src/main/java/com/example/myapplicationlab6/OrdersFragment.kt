package com.example.myapplicationlab6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch

class OrdersFragment : Fragment() {

    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_orders, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = root.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.orders_recycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            try {
                val api = RetrofitClient.createApiService(requireContext())
                val r = api.getMyOrders(1, 50)
                if (r.isSuccessful) {
                    val body = r.body()
                    val orders = body?.data ?: emptyList()
                    recycler.adapter = object : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
                        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
                            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
                            return object : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {}
                        }

                        override fun getItemCount(): Int = orders.size

                        override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
                            val order = orders[position]
                            val tvName = holder.itemView.findViewById<TextView>(R.id.product_name)
                            val tvPrice = holder.itemView.findViewById<TextView>(R.id.product_price)
                            tvName.text = "Заказ #${order.id}"
                            tvPrice.text = order.status ?: ""
                        }
                    }
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}
