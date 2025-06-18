package com.mdlb.project.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mdlb.project.R
import com.mdlb.project.activities.NotesActivity
import com.mdlb.project.models.NoteModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotesAdapter(
    private var notesList: List<NoteModel>,
    private val onEdit: (NoteModel) -> Unit,
    private val onDelete: (NoteModel) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val content: TextView = view.findViewById(R.id.subtitle)
        val date: TextView = view.findViewById(R.id.created_at)
        val cardHolder: MaterialCardView = view.findViewById(R.id.note_card)
        val editButton: Button = view.findViewById(R.id.editButton)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)

        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.title.text = note.title
        holder.content.text = note.content
        holder.date.text = "Created at: " + SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).format(note.timestamp ?: Date())

        holder.editButton.setOnClickListener { onEdit(note) }
        holder.deleteButton.setOnClickListener { onDelete(note) }
    }

    override fun getItemCount() = notesList.size

    fun updateList(newNotes: List<NoteModel>) {
        notesList = newNotes
        notifyDataSetChanged()
    }
}