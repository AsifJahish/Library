package com.example.myprojectlibrary.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myprojectlibrary.Model.Users
import com.example.myprojectlibrary.R
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var dbref: DatabaseReference
    private lateinit var emailL: EditText
    private lateinit var passwordL: EditText
    private lateinit var login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailL = findViewById(R.id.emailLogin)
        passwordL = findViewById(R.id.passwordLogin)
        login = findViewById(R.id.loginBtn)

        dbref = FirebaseDatabase.getInstance().reference.child("users")

        login.setOnClickListener {
            val email = emailL.text.toString()
            val password = passwordL.text.toString()

            dbref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var isAdmin = false
                    var isUser = false
                    var userId: String? = null

                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(Users::class.java)
                        if (user?.email == email && user.password == password) {
                            if (user.email == "admin" && user.password=="admin") {
                                // Navigate to admin activity
                                isAdmin = true
                                break
                            } else {
                                // Navigate to user activity
                                isUser = true
                                userId = userSnapshot.key
                                break
                            }
                        }
                    }

                    if (isAdmin) {
                        // Start admin activity
                        Toast.makeText(this@LoginActivity, "Logged in as admin", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, Admin::class.java)
                        startActivity(intent)
                    } else if (isUser) {
                        Toast.makeText(this@LoginActivity, "Logged in as user", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, UserActivity::class.java)
                        intent.putExtra("us", userId) // Pass the user ID to UserActivity
                        startActivity(intent)

                    } else {
                        // Invalid credentials
                        Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                    Toast.makeText(this@LoginActivity, "Database error: " + databaseError.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
