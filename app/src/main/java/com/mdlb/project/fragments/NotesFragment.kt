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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mdlb.project.models.NoteModel
import com.mdlb.project.R
import com.mdlb.project.adapters.NotesAdapter

class NotesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chk_select_all: CheckBox
    private lateinit var btn_delete_all: Button

    private var notesList: ArrayList<NoteModel> = ArrayList()
    private var notesAdapter: NotesAdapter? = null

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
        chk_select_all = view.findViewById(R.id.chk_select_all)
        btn_delete_all = view.findViewById(R.id.btn_delete_all)

        // Try to fetch the notes collection of the user using Firestore


        notesList.add(NoteModel().apply { title = "Title 1"; content = "Content 1"; date = "2024-12-01"; id = "1"; })
        notesList.add(NoteModel().apply { title = "Title 2"; content = "Content 2"; date = "2024-12-01"; id = "2"; })
        notesList.add(NoteModel().apply { title = "Title 3"; content = "Content 3"; date = "2024-12-01"; id = "3"; })
        notesList.add(NoteModel().apply { title = "Title 4"; content = "Content 4"; date = "2024-12-01"; id = "4"; })
        notesList.add(NoteModel().apply { title = "Title 5"; content = "Content 5"; date = "2024-12-01"; id = "5"; })
        notesList.add(NoteModel().apply { title = "Title 6"; content = "Content 6"; date = "2024-12-01"; id = "6"; })
        notesList.add(NoteModel().apply { title = "Title 7"; content = "Content 7"; date = "2024-12-01"; id = "7"; })
        notesList.add(NoteModel().apply { title = "Title 8"; content = "Content 8"; date = "2024-12-01"; id = "8"; })

        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this.context))
        notesAdapter = NotesAdapter(notesList)
        recyclerView.setAdapter(notesAdapter)

        chk_select_all.setOnClickListener {
            chkSelectAll()
        }

        btn_delete_all.setOnClickListener {
            btnDeleteAll()
        }

        return view
    }

    private fun chkSelectAll() {
        if (chk_select_all.isChecked) {
            for (note in notesList) {
                note.isSelected = true
            }
        } else {
            for (note in notesList) {
                note.isSelected = false
            }
        }
    }

    private fun btnDeleteAll() {
        if (chk_select_all.isChecked) {
            notesList.clear()
            notesAdapter?.notifyDataSetChanged()
            chk_select_all.isChecked = false
        } else {
            Snackbar.make(requireView(), "Please select all items first", Snackbar.LENGTH_SHORT).show()
            notesAdapter?.notifyDataSetChanged()
        }
    }
}