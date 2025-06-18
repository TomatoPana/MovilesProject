package com.mdlb.project.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.mdlb.project.models.NoteModel
import com.mdlb.project.R
import com.mdlb.project.activities.NotesActivity
import com.mdlb.project.adapters.NotesAdapter
import com.mdlb.project.viewmodels.NoteViewModel

class NotesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var notesAdapter: NotesAdapter
    private lateinit var viewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_notes, container, false)

        view = initControllers(view)

        return view
    }

    private fun initControllers(view: View): View {

        recyclerView = view.findViewById(R.id.recycler_view)

        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        notesAdapter = NotesAdapter(
            emptyList(),
            onEdit = { note ->
                val intent = Intent(this.context, NotesActivity::class.java)
                this.activity?.startActivity(intent, null)
            },
            onDelete = { note ->
                MaterialAlertDialogBuilder(this.requireContext())
                    .setTitle("Confirm action")
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.deleteNote(note.id)
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
        })

        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this.context))

        recyclerView.setAdapter(notesAdapter)

        viewModel.notes.observe(viewLifecycleOwner) {notes ->
            notesAdapter.updateList(notes)
        }


        return view
    }
}