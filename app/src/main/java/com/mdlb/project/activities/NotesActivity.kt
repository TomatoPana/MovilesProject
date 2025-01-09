package com.mdlb.project.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.textfield.TextInputLayout
import com.mdlb.project.R

class NotesActivity : AppCompatActivity() {

    private var isEdit: Boolean = false
    private var noteId: String? = null
    private lateinit var titleTextField: TextInputLayout
    private lateinit var contentTextField: TextInputLayout
    private lateinit var fromDeviceButton: Button
    private lateinit var fromCameraButton: Button
    private lateinit var previewConstraintLayout: ConstraintLayout
    private lateinit var previewImage: ImageView
    private lateinit var deleteImageButton: Button
    private lateinit var mapHolder: FragmentContainerView
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        intent.getStringExtra("id")?.let {
            noteId = it
            isEdit = true
        }

        initElements()

    }

    private fun initElements() {
        titleTextField = findViewById(R.id.titleTextField)
    }
}