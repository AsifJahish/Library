package com.example.myprojectlibrary.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myprojectlibrary.Fragments.Home
import com.example.myprojectlibrary.Fragments.Insert
import com.example.myprojectlibrary.Fragments.UserListFragment
import com.example.myprojectlibrary.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class Admin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)


        val nav= findViewById<BottomNavigationView>(R.id.bottomnavigation)

        nav.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(Home())
                R.id.add -> replaceFragment(Insert())
                R.id.person -> replaceFragment(UserListFragment())

                else -> {
                    // Handle other cases
                }
            }
            true
        }


    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }


}