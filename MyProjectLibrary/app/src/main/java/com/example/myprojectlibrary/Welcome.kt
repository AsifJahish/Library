package com.example.myprojectlibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myprojectlibrary.activity.Admin
import com.example.myprojectlibrary.activity.LoginActivity
import com.example.myprojectlibrary.activity.SignUpActivity

class Welcome : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val signUp= findViewById<Button>(R.id.signUpButton)
        val loginBtn = findViewById<Button>(R.id.login)

        signUp.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        loginBtn.setOnClickListener{
            val intent= Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }



    }
}