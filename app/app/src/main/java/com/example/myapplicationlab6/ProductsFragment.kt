package com.example.myapplicationlab6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.util.Log
import android.widget.TextView
import android.widget.ScrollView
import com.google.gson.Gson
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch

class ProductsFragment : Fragment() {

    private lateinit var bindingRoot: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindingRoot = inflater.inflate(R.layout.fragment_products, container, false)
        return bindingRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = bindingRoot.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.products_recycler)
        val progress = bindingRoot.findViewById<View>(R.id.progress)
        val emptyText = bindingRoot.findViewById<TextView>(R.id.empty_text)
        val responseContainer = bindingRoot.findViewById<ScrollView>(R.id.response_container)
        val responseText = bindingRoot.findViewById<TextView>(R.id.response_text)

        recycler.layoutManager = LinearLayoutManager(requireContext())

        progress.visibility = View.VISIBLE

        val api = RetrofitClient.createApiService(requireContext())

        lifecycleScope.launch {
            try {
                val resp = api.getProducts(1, 50, null)
                if (resp.isSuccessful) {
                    val body = resp.body()
                    val data = body?.data ?: emptyList()
                    // show full response JSON for debugging
                    try {
                        val json = Gson().toJson(body)
                        responseText.text = json
                        responseContainer.visibility = View.VISIBLE
                    } catch (_: Exception) { /* ignore */ }
                    recycler.adapter = ProductAdapter(data) { product ->
                        // navigate to details
                        val bundle = Bundle()
                        bundle.putInt("productId", product.id)
                        bundle.putString("productName", product.name)
                        bundle.putString("productImage", product.image)
                        bundle.putDouble("productPrice", product.amount ?: 0.0)
                        findNavController().navigate(R.id.productDetailFragment, bundle)
                    }
                    if (data.isEmpty()) {
                        emptyText.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Список продуктов пуст", Toast.LENGTH_SHORT).show()
                    } else {
                        emptyText.visibility = View.GONE
                    }
                } else {
                    val raw = try { resp.errorBody()?.string() } catch (e: Exception) { null }
                    // show raw error or http reason
                    val msg = raw ?: "HTTP ${resp.code()}: ${resp.message()}"
                    responseText.text = msg
                    responseContainer.visibility = View.VISIBLE
                    val err = "product list failed: ${resp.code()} ${resp.message()}"
                    Log.w("ProductsFragment", err)
                    Toast.makeText(requireContext(), "Не удалось загрузить продукты (код ${resp.code()})", Toast.LENGTH_SHORT).show()
                    emptyText.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Log.e("ProductsFragment", "getProducts failed", e)
                e.printStackTrace()
                Toast.makeText(requireContext(), "Ошибка при загрузке продуктов: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                emptyText.visibility = View.VISIBLE
            } finally {
                progress.visibility = View.GONE
            }
        }
    }
}
