package com.mdlb.project.activities

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.mdlb.project.R
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.Date
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

class NotesActivity : AppCompatActivity(), OnMapReadyCallback {

    private var isEdit: Boolean = false
    private var isDirty: Boolean = true
    private var noteId: String? = null

    private var imageUri: Uri? = null

    private lateinit var titleTextField: TextInputLayout
    private lateinit var contentTextField: TextInputLayout
    private lateinit var fromDeviceButton: Button
    private lateinit var fromCameraButton: Button
    private lateinit var previewConstraintLayout: ConstraintLayout
    private lateinit var previewImage: ImageView
    private lateinit var deleteImageButton: Button
    private lateinit var mapHolder: FragmentContainerView
    private lateinit var saveButton: Button
    private lateinit var dateTextField: TextInputLayout

    private lateinit var mMap: GoogleMap
    private var locationMarker: Marker? = null
    private var reminderLocation: LatLng? = null

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        uri: Uri? -> uri?.let {
            imageUri = it
            previewImage.setImageURI(it)
            previewConstraintLayout.visibility = View.VISIBLE
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        success: Boolean -> if (success) {
            imageUri?.let {
                previewImage.setImageURI(it)
                previewConstraintLayout.visibility = View.VISIBLE
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action
                launchCamera()
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied.
                Toast.makeText(this, "Camera permission is required to take photos.", Toast.LENGTH_LONG).show()
            }
        }

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

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapHolder) as SupportMapFragment
        mapFragment.getMapAsync(this)

        previewConstraintLayout.visibility = View.GONE

        fromDeviceButton.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        fromCameraButton.setOnClickListener {
            checkCameraPermissionAndLaunch()
        }

        deleteImageButton.setOnClickListener {
            imageUri = null
            previewImage.setImageURI(null)
            previewConstraintLayout.visibility = View.GONE
        }

        dateTextField.editText?.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            datePicker.show(supportFragmentManager, "DATE_PICKER")

            // Set the listener for when a date is selected
            datePicker.addOnPositiveButtonClickListener { selection ->
                // The 'selection' is the date in milliseconds since the UTC epoch.

                // We need to format it to a human-readable string.
                // Note that the date picker returns a UTC timestamp. We must account for the
                // timezone offset to display the correct local date.

                // 1. Create a SimpleDateFormat
                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
                // 2. Set the timezone to UTC to interpret the timestamp correctly
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                // 3. Format the date
                val formattedDate = dateFormat.format(Date(selection))

                // Set the formatted date to the TextInputEditText
                dateTextField.editText?.setText(formattedDate)
            }
        }

        saveButton.setOnClickListener {}
    }

    private fun initElements() {
        titleTextField = findViewById(R.id.titleTextField)
        contentTextField = findViewById(R.id.contentTextField)
        fromDeviceButton = findViewById(R.id.fromDeviceButton)
        fromCameraButton = findViewById(R.id.fromCameraButton)
        previewConstraintLayout = findViewById(R.id.previewConstraintLayout)
        previewImage = findViewById(R.id.previewImage)
        deleteImageButton = findViewById(R.id.deleteImageButton)
        mapHolder = findViewById(R.id.mapHolder)
        saveButton = findViewById(R.id.saveButton)
        dateTextField = findViewById(R.id.dateTextField)
    }

    private fun preventBackOnPendingChanges() {
        if (!isDirty) return

        MaterialAlertDialogBuilder(this.baseContext)
            .setTitle("Confirm action")
            .setMessage("Are you sure you want to exit without saving?")
            .setPositiveButton("Yes") { _, _ ->
                // The user confirmed the exit, continue with the normal flow
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun createImageUri(): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "reminder_image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }

    private fun checkCameraPermissionAndLaunch() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // The permission is already granted, launch the camera.
                launchCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // You can show a custom dialog here to explain to the user why you need the permission.
                // For this example, we'll just request the permission directly.
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            else -> {
                // Directly request for the permission
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun launchCamera() {
        imageUri = createImageUri()
        // The imageUri is non-null at this point, so we can safely launch.
        takePictureLauncher.launch(imageUri!!)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // *** SET INITIAL VIEW TO GUADALAJARA ***
        // Define the coordinates for Guadalajara
        val guadalajara = LatLng(20.6597, -103.3496)
        // Set the initial camera position and zoom level
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(guadalajara, 11f))

        // TODO: If a note exists, reload the map with the location
        // note?.let {
        //     placeMarkerOnMap(it)
        //     googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
        // }

        mMap.setOnMapLongClickListener { latLng ->
            handleLongPress(latLng)
        }
    }

    private fun handleLongPress(latLng: LatLng) {
        // If a marker already exists, remove it
        locationMarker?.remove()

        // Add a new marker to the map at the long-pressed location
        placeMarkerOnMap(latLng)

        // Store the selected latitude and longitude
        reminderLocation = latLng

        // You can now save `reminderLocation.latitude` and `reminderLocation.longitude`
        // to your Firebase database when the user saves the reminder.
    }

    private fun placeMarkerOnMap(latLng: LatLng) {
        val markerOptions = MarkerOptions().position(latLng).title("Reminder Location")
        locationMarker = mMap.addMarker(markerOptions)
    }

    private fun removeLocation() {
        locationMarker?.remove()
        locationMarker = null
        reminderLocation = null
        // Update your Firebase database to remove the location for this reminder.
    }

    fun canScheduleExactAlarms(context: Context): Boolean {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true // Permission is granted by default on older versions
        }
    }

    fun requestScheduleExactAlarmPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            context.startActivity(intent)
        }
    }
}