package com.example.myprojectlibrary.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myprojectlibrary.Fragments.Home
import com.example.myprojectlibrary.Fragments.Insert
import com.example.myprojectlibrary.Fragments.ProfileFragment
import com.example.myprojectlibrary.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class UserActivity : AppCompatActivity() {
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)


        val userId = intent.getStringExtra("us")
        val profileFragment = ProfileFragment.newInstance(userId)

        val nav = findViewById<BottomNavigationView>(R.id.bottomnavigation)

        nav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(Home())
                R.id.personUser -> replaceFragment(ProfileFragment.newInstance(userId))
                else -> {
                    // Handle other cases
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}