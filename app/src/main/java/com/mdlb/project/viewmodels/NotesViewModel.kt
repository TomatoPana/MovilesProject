package com.mdlb.project.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mdlb.project.models.NoteModel

class NoteViewModel: ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val _notes = MutableLiveData<List<NoteModel>>()
    val notes: LiveData<List<NoteModel>> = _notes

    init {
        loadNotes()
    }

    private fun loadNotes() {
        val user = auth.currentUser ?: return
        firestore.collection("users")
            .document(user.uid)
            .collection("notes")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val notes = snapshot.documents.map {
                    val note = it.toObject(NoteModel::class.java)
                    note?.copy(id = it.id) ?: NoteModel()
                }
                _notes.postValue(notes)
            }
    }

    fun addNote(title: String, content: String) {
        val user = auth.currentUser ?: return
        val note = NoteModel(title = title, content = content)
        firestore.collection("users")
                .document(user.uid)
                .collection("notes")
                .add(note)
    }

    fun updateNote(note: NoteModel) {
        val user = auth.currentUser ?: return
        firestore.collection("users")
                        .document(user.uid)
                        .collection("notes")
                        .document(note.id)
                        .set(note)
    }

    fun deleteNote(noteId: String) {
        val user = auth.currentUser ?: return
        firestore.collection("users")
                        .document(user.uid)
                        .collection("notes")
                        .document(noteId)
                        .delete()
    }
}