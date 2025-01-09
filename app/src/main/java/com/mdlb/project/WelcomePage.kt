package com.mdlb.project

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mdlb.project.activities.NotesActivity
import com.mdlb.project.fragments.AlarmsFragment
import com.mdlb.project.fragments.MapsFragment
import com.mdlb.project.fragments.NotesFragment
import kotlin.system.exitProcess

class WelcomePage : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var fab: FloatingActionButton
    private var screen = "notes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkForPermissions()
        setContentView(R.layout.activity_welcome_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        onBackPressedDispatcher.addCallback { finishAffinity() }

        fab = findViewById(R.id.floating_action_button)
        fab.setOnClickListener {
            when (screen) {
                "notes" -> {
                    val intent = Intent(baseContext, NotesActivity::class.java)
                    startActivity(intent)
                }
                "alarms" -> {
                    val intent = Intent(baseContext, NotesActivity::class.java)
                    startActivity(intent)
                }
                else -> { true }
            }
        }

        loadFragment(NotesFragment())
        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.notes -> {
                    screen = "notes"
                    fab.show()
                    loadFragment(NotesFragment())
                    true
                }
                R.id.alarms -> {
                    screen = "alarms"
                    fab.show()
                    loadFragment(AlarmsFragment())
                    true
                }
                R.id.maps -> {
                    fab.hide()
                    loadFragment(MapsFragment())
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    private fun checkForPermissions() {

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}