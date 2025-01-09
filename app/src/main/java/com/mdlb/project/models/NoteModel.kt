package com.mdlb.project.models

import java.io.Serializable

class NoteModel : Serializable {

    var title: String? = null
    var content: String? = null
    var date: String? = null
    var photoUrl: String? = null
    var id: String? = null
    var isSelected: Boolean = false

    constructor() {}

    constructor(title: String?, content: String?, date: String?, photoUrl: String?, id: String?) {
        this.title = title
        this.content = content
        this.date = date
        this.photoUrl = photoUrl
        this.id = id
        this.isSelected = false
    }

}