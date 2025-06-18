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

class NotesAdapter(private var notesList: ArrayList<NoteModel>) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = notesList[position].title
        holder.content.text = notesList[position].content
        holder.date.text = notesList[position].date
        holder.photo.visibility = if (notesList[position].photoUrl != null) View.VISIBLE else View.GONE
        holder.cardHolder.setOnLongClickListener {
            notesList[position].isSelected = !notesList[position].isSelected
            notifyDataSetChanged()
            true
        }
        holder.cardHolder.setOnClickListener {
            if (notesList[position].isSelected) {
                notesList[position].isSelected = false
            } else {
                // Open note
            }
            notifyDataSetChanged()
        }
        holder.editButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, NotesActivity::class.java)
            startActivity(holder.itemView.context, intent, null)
        }
        holder.deleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(holder.itemView.context)
                .setTitle("Confirm action")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes") { _, _ ->
                    notesList.removeAt(position)
                    notifyDataSetChanged()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val title = view.findViewById<TextView>(R.id.title)
        val content = view.findViewById<TextView>(R.id.subtitle)
        val date = view.findViewById<TextView>(R.id.created_at)
        val photo = view.findViewById<ImageView>(R.id.img_note)
        val cardHolder = view.findViewById<MaterialCardView>(R.id.note_card)
        val editButton = view.findViewById<Button>(R.id.editButton)
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)

    }
}