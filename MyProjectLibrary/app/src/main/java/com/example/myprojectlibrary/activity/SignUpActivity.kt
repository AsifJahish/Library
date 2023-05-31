package com.example.myprojectlibrary.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myprojectlibrary.Model.Books
import com.example.myprojectlibrary.Model.Users
import com.example.myprojectlibrary.R
import com.example.myprojectlibrary.databinding.ActivityDetailsBinding
import com.example.myprojectlibrary.databinding.ActivitySignUpBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var dbref: DatabaseReference
    private lateinit var signupBtn:Button
    private lateinit var nameE: EditText
    private lateinit var emailE:EditText
    private lateinit var passwordE:EditText
    private lateinit var conE:EditText
    private lateinit var idE:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        dbref = FirebaseDatabase.getInstance().reference.child("users")

        signupBtn= findViewById(R.id.facebookBtn)
        nameE = findViewById(R.id.editTextName)
        emailE = findViewById(R.id.editTextEmail)
        passwordE = findViewById(R.id.editTextPassword)
        conE = findViewById(R.id.editTextConfirmPassword)
        signupBtn = findViewById(R.id.facebookBtn)
        idE= findViewById(R.id.editTextID)
        signupBtn.setOnClickListener{
            saveUser()
            val intent21= Intent(this, LoginActivity::class.java)
            startActivity(intent21)


        }

    }

    private fun saveUser() {
        val name = nameE.text.toString()
        val email = emailE.text.toString()
        val password = passwordE.text.toString()
        val con = conE.text.toString()
        val id = idE.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || id.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
        } else if (con.isEmpty() || con != password) {
            Toast.makeText(this, "Password and confirm password do not match", Toast.LENGTH_SHORT).show()
        } else {
            val users = Users(id, name, email, password, con)
            dbref.child(id).setValue(users) // Use id as the key
                .addOnCompleteListener {
                    Toast.makeText(this, "User registration completed", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { err ->
                    Toast.makeText(this, "Error: ${err.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }


}