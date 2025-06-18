package com.mdlb.project.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.Geofence
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mdlb.project.models.NoteModel
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.mdlb.project.geofencing.GeofenceHelper

class NoteViewModel: ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val _notes = MutableLiveData<List<NoteModel>>()
    val notes: LiveData<List<NoteModel>> = _notes
    // In your ViewModel or Repository
    private lateinit var geofencingClient: GeofencingClient

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

    fun addNote(
        title: String,
        content: String,
        photoUrl: String? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        date: String? = null,
        time: String? = null,
        enableAlarms: Boolean = false
    ) {
        val user = auth.currentUser ?: return
        val hasLocation = latitude != null && longitude != null
        val note = NoteModel(
            title = title,
            content = content,
            photoUrl = photoUrl,
            latitude = latitude,
            longitude = longitude,
            hasLocation = hasLocation,
            date = date,
            time = time,
            enableAlarms = enableAlarms
        )
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