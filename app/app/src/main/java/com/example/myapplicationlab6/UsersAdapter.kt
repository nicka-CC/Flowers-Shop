package com.example.myapplicationlab6

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplicationlab6.databinding.ItemRequestBinding

class UsersAdapter(
    private var users: List<Contact>,
    private val onAdd: (Contact) -> Unit
) : RecyclerView.Adapter<UsersAdapter.VH>() {

    inner class VH(val binding: ItemRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(u: Contact) {
            val core = binding.contactCore
            core.userName.text = u.name
            core.userLogin.text = u.login
            Glide.with(core.root)
                .load(u.avatarUrl)
                .placeholder(R.drawable.ic_placeholder_avatar)
                .error(R.drawable.ic_placeholder_avatar)
                .circleCrop()
                .into(core.imageAvatar)

            // Прячем кнопку отклонения, и меняем текст кнопки принятия
            binding.btnDecline.visibility = View.GONE
            binding.btnAccept.text = "+"
            binding.btnAccept.setOnClickListener { onAdd(u) }
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, vt: Int): VH {
        val b = ItemRequestBinding.inflate(
            LayoutInflater.from(p.context),
            p, false
        )
        return VH(b)
    }

    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(users[pos])
    override fun getItemCount(): Int = users.size

    fun update(list: List<Contact>) {
        users = list
        notifyDataSetChanged()
    }
}
