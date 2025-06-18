package com.mdlb.project.models

import java.io.Serializable

data class NoteModel(
    var title: String? = null,
    var content: String? = null,
    var date: String? = null,
    var photoUrl: String? = null,
    var id: String? = null,
    var isSelected: Boolean = false,
) : Serializable