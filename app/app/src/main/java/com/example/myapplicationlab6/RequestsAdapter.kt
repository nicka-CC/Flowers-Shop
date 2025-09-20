package com.example.myapplicationlab6

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplicationlab6.databinding.ItemRequestBinding

class RequestsAdapter(
    private val requests: List<ContactRequest>,
    private val onAccept: (ContactRequest) -> Unit,
    private val onDecline: (ContactRequest) -> Unit,
    private val onItemHandled: () -> Unit
) : RecyclerView.Adapter<RequestsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(req: ContactRequest) {
            // 1) Достаём core-binding
            val core = binding.contactCore

            // 2) Заполняем данные
            core.userName.text = req.name
            core.userLogin.text = req.login
            Glide.with(core.root)
                .load(req.avatar)
                .placeholder(R.drawable.ic_placeholder_avatar)
                .error(R.drawable.ic_placeholder_avatar)
                .circleCrop()
                .into(core.imageAvatar)

            // 3) Обработчики кнопок
            binding.btnAccept.setOnClickListener {
                onAccept(req)
                onItemHandled()
            }
            binding.btnDecline.setOnClickListener {
                onDecline(req)
                onItemHandled()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemRequestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(requests[position])

    override fun getItemCount(): Int = requests.size
}


