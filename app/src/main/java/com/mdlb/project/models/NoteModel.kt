package com.mdlb.project.models

import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.Date

data class NoteModel(
    var id: String = "",
    var title: String = "",
    var content: String = "",

    @ServerTimestamp
    var timestamp: Date? = null,

    var photoUrl: String? = null,

    var latitude: Double? = null,
    var longitude: Double? = null,
    var hasLocation: Boolean = false,

    var date: String? = null,
    var time: String? = null,
    var enableAlarms: Boolean = false

) : Serializable