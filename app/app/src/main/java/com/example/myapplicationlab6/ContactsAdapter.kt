package com.example.myapplicationlab6

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationlab6.databinding.ItemContactBinding
import com.bumptech.glide.Glide


class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ViewHolder>(), Filterable {

    private var contacts = mutableListOf<Contact>()
    private var filteredContacts = mutableListOf<Contact>()

    inner class ViewHolder(val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setContacts(newContacts: List<Contact>) {
        contacts = newContacts.toMutableList()
        filteredContacts = newContacts.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = filteredContacts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        val binding = ItemContactBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = filteredContacts[position]
        with(holder.binding) {
            userName.text = contact.name
            userLogin.text = contact.login
            Glide.with(root)
                .load(contact.avatarUrl)
                .placeholder(R.drawable.ic_placeholder_avatar)
                .error(R.drawable.ic_placeholder_avatar)
                .circleCrop()
                .into(imageAvatar)
        }
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val result = FilterResults()
            result.values = if (constraint.isNullOrEmpty()) contacts else
                contacts.filter {
                    it.name.contains(constraint, ignoreCase = true)
                }
            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredContacts = (results?.values as? List<Contact>)?.toMutableList()
                ?: mutableListOf()
            notifyDataSetChanged()
        }
    }
}
