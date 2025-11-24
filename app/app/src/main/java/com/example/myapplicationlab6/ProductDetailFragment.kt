package com.example.myapplicationlab6

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class ProductDetailFragment : Fragment() {

    private lateinit var bindingRoot: View

    private var productId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindingRoot = inflater.inflate(R.layout.fragment_product_detail, container, false)
        return bindingRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productId = arguments?.getInt("productId") ?: 0
        val name = arguments?.getString("productName")
        val image = arguments?.getString("productImage")
        val price = arguments?.getDouble("productPrice") ?: 0.0

        val img = bindingRoot.findViewById<android.widget.ImageView>(R.id.product_image_large)
        val nameTv = bindingRoot.findViewById<android.widget.TextView>(R.id.product_name_title)
        val priceTv = bindingRoot.findViewById<android.widget.TextView>(R.id.product_price_text)
        val addToCartBtn = bindingRoot.findViewById<android.view.View>(R.id.add_to_cart_btn)
        val addCommentBtn = bindingRoot.findViewById<android.view.View>(R.id.add_comment_btn)
        val commentsRecycler = bindingRoot.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.comments_recycler)

        nameTv.text = name ?: "—"
        priceTv.text = price.toString()
        Glide.with(requireContext()).load(image).placeholder(R.drawable.ic_placeholder_avatar).into(img)

        commentsRecycler.layoutManager = LinearLayoutManager(requireContext())

        // Load comments (shopId unknown -> pass 0)
        val api = RetrofitClient.createApiService(requireContext())
        lifecycleScope.launch {
            try {
                val resp = api.getProductComments(0, productId)
                if (resp.isSuccessful) {
                    val body = resp.body()
                    commentsRecycler.adapter = CommentAdapter(body?.data ?: emptyList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        addToCartBtn.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val r = api.addToCart(AddToCartRequest(productId, 1))
                    if (r.isSuccessful) {
                        Toast.makeText(requireContext(), "Добавлено в корзину", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Ошибка добавления", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) { e.printStackTrace(); Toast.makeText(requireContext(), "Ошибка", Toast.LENGTH_SHORT).show() }
            }
        }

        addCommentBtn.setOnClickListener {
            showAddCommentDialog(api)
        }
    }

    private fun showAddCommentDialog(api: ApiService) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Добавить комментарий")
        val input = EditText(requireContext())
        input.hint = "Текст комментария"
        builder.setView(input)
        builder.setPositiveButton("Отправить") { dlg, _ ->
            val text = input.text.toString()
            lifecycleScope.launch {
                try {
                    val map = mapOf("name" to "Mobile", "description" to text, "about" to "", "image" to "")
                    val resp = api.postProductComment(productId, map)
                    if (resp.isSuccessful) {
                        Toast.makeText(requireContext(), "Комментарий отправлен", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Ошибка отправки", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) { e.printStackTrace(); Toast.makeText(requireContext(), "Ошибка", Toast.LENGTH_SHORT).show() }
            }
            dlg.dismiss()
        }
        builder.setNegativeButton("Отмена") { dlg, _ -> dlg.dismiss() }
        builder.show()
    }

}
