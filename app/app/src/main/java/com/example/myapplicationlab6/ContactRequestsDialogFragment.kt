package com.example.myapplicationlab6

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ContactRequestsDialogFragment : DialogFragment() {

    private lateinit var requests: List<ContactRequest>
    private lateinit var onAccept: (ContactRequest) -> Unit
    private lateinit var onDecline: (ContactRequest) -> Unit
    private lateinit var onItemHandled: () -> Unit

    companion object {
        fun newInstance(
            requests: List<ContactRequest>,
            onAccept: (ContactRequest) -> Unit,
            onDecline: (ContactRequest) -> Unit,
            onItemHandled: () -> Unit
        ): ContactRequestsDialogFragment {
            return ContactRequestsDialogFragment().apply {
                this.requests = requests
                this.onAccept = onAccept
                this.onDecline = onDecline
                this.onItemHandled = onItemHandled
            }
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_requests_list, null)
        val recycler = view.findViewById<RecyclerView>(R.id.rvRequests)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = RequestsAdapter(
            requests = requests,
            onAccept = { req -> onAccept(req) },
            onDecline = { req -> onDecline(req) },
            onItemHandled = { dismiss() }
        )



        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setNegativeButton("Закрыть", null)
            .create()
    }
}
