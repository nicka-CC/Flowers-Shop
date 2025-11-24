package com.example.myapplicationlab6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class UserInfoFragment : Fragment() {

    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_user_info, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val face = root.findViewById<ImageView>(R.id.user_face)
        val nameTv = root.findViewById<TextView>(R.id.user_name_tv)
        val ordersBtn = root.findViewById<Button>(R.id.my_orders_btn)

        val api = RetrofitClient.createApiService(requireContext())
        lifecycleScope.launch {
            try {
                val r = api.getMe()
                if (r.isSuccessful) {
                    val u = r.body()
                    nameTv.text = u?.name ?: "—"
                    Glide.with(requireContext()).load(u?.avatar).placeholder(R.drawable.ic_placeholder_avatar).into(face)
                }
            } catch (e: Exception) { e.printStackTrace() }
        }

        ordersBtn.setOnClickListener {
            findNavController().navigate(R.id.ordersFragment)
        }
    }
}
