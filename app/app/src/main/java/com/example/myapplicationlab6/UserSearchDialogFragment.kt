package com.example.myapplicationlab6

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplicationlab6.databinding.DialogUsersListBinding

class UserSearchDialogFragment : DialogFragment() {

    // 1) Правильный Binding-класс для dialog_users_list.xml
    private var _binding: DialogUsersListBinding? = null
    private val binding get() = _binding!!

    // 2) Разделяем ViewModel с родительским фрагментом
    private val viewModel: ContactsViewModel by activityViewModels {
        ContactsViewModel.getViewModelFactory(
            RetrofitClient.createApiService(requireContext())
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 3) Инфлейтим через Binding
        _binding = DialogUsersListBinding.inflate(layoutInflater)
        val view = binding.root

        // 4) Настраиваем RecyclerView и адаптер
        val adapter = UsersAdapter(emptyList()) { user ->
            viewModel.sendContactRequest(user)
            dismiss()
        }
        binding.rvSearchUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchUsers.adapter = adapter

        // 5) SearchView с debounce
        binding.svSearchUsers.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.onSearchQuery(it) }
                return true
            }
        })

        // 6) Наблюдаем за результатами поиска и состоянием загрузки
        viewModel.searchResults.observe(this) { list ->
            adapter.update(list)
        }
        viewModel.isSearching.observe(this) { loading ->
            binding.pgSearchUsers.visibility = if (loading) View.VISIBLE else View.GONE
            binding.rvSearchUsers.visibility = if (loading) View.GONE else View.VISIBLE
        }



        // 7) Собираем диалог
        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setNegativeButton("Закрыть", null)
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
