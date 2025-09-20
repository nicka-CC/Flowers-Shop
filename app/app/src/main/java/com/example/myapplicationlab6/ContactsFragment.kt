package com.example.myapplicationlab6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.util.Log
import com.example.myapplicationlab6.databinding.FragmentContactsBinding
import androidx.recyclerview.widget.LinearLayoutManager

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ContactsAdapter

    private val apiService by lazy {
        RetrofitClient.createApiService(requireContext())
    }

    private val viewModel by viewModels<ContactsViewModel> {
        ContactsViewModel.getViewModelFactory(apiService)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ContactsAdapter()
        binding.rvSearchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchResults.adapter = adapter

        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            adapter.setContacts(contacts)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.rvSearchResults.visibility = if (isLoading) View.GONE else View.VISIBLE

        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        binding.fabAddContact.setOnClickListener {
            UserSearchDialogFragment().show(childFragmentManager, "userSearch")
        }

        binding.btnViewRequests.setOnClickListener {
            viewModel.loadIncomingRequests()
        }

        viewModel.requests.observe(viewLifecycleOwner) { requests ->
            if (requests.isNotEmpty()) {
                ContactRequestsDialogFragment.newInstance(
                    requests = requests,
                    onAccept = { request -> viewModel.acceptRequest(request.requestId) },
                    onDecline = { request -> viewModel.declineRequest(request.requestId) },
                    onItemHandled = {
                        viewModel.loadIncomingRequests()
                        viewModel.updateContacts()
                    }
                ).show(childFragmentManager, "requestsDialog")
            }
        }

        viewModel.updateContacts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

