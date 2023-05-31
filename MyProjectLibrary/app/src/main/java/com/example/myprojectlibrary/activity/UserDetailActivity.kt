package com.example.myprojectlibrary.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myprojectlibrary.Model.BorrowedBook
import com.example.myprojectlibrary.Model.Users
import com.example.myprojectlibrary.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserDetailActivity : AppCompatActivity() {
    private lateinit var dbref: DatabaseReference
    private lateinit var userID: TextView
    private lateinit var userName: TextView
    private lateinit var useremail: TextView
    private lateinit var bookISBN: EditText
    private lateinit var bookNe: EditText
    private lateinit var bookName: TextView
    private lateinit var borrowedISBN: TextView

    private lateinit var borrowBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        userID = findViewById(R.id.userIdTextView)
        userName = findViewById(R.id.nameTextView)
        useremail = findViewById(R.id.emailTextView)
        dbref = FirebaseDatabase.getInstance().reference

        borrowBtn = findViewById(R.id.borrow)
        bookISBN = findViewById(R.id.bookIsbn)
        bookNe = findViewById(R.id.bookNe)

        bookName = findViewById(R.id.bookNameBorrowed)
        borrowedISBN = findViewById(R.id.borrowISBN)

        val userId = intent.getStringExtra("Id")
        if (userId.isNullOrEmpty()) {
            // Handle the case when userId is null or empty
            return
        }

        getUserData(userId)

        borrowBtn.setOnClickListener {
            putBorrow(userId)
        }
    }

    private fun getUserData(userId: String) {
        val userRef = dbref.child("users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(Users::class.java)
                    if (user != null) {
                        userID.text =  user.userId
                        userName.text =   user.name
                        useremail.text =   user.email

                        getLastBBook(userId)
                    }
                } else {
                    // Handle the case when the user data does not exist in the database
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that occur during the data retrieval process
            }
        })
    }

    private fun putBorrow(userId: String) {
        val isBN = bookISBN.text.toString()
        val nm = bookNe.text.toString()
        val uiD = userID.text.toString()
        val unamE = userName.text.toString()

        if (isBN.isEmpty() || nm.isEmpty()) {
            Toast.makeText(this, "Enter the name and ISBN of the book", Toast.LENGTH_SHORT).show()
        } else {
            val booksRef = FirebaseDatabase.getInstance().getReference("books")
            booksRef.child(isBN).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        dbref = FirebaseDatabase.getInstance().reference.child("Borrow")
                        val bBookRef = dbref.push()
                        val bBookKey = bBookRef.key // Generated unique key

                        val bBooks = BorrowedBook(bBookKey, uiD, unamE, isBN, nm)

                        bBookRef.setValue(bBooks)
                            .addOnCompleteListener {
                                Toast.makeText(this@UserDetailActivity, "Completed", Toast.LENGTH_SHORT)
                            }.addOnFailureListener { err ->
                                Toast.makeText(this@UserDetailActivity, "Error: ${err.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        // Book with the given ISBN does not exist
                        Toast.makeText(this@UserDetailActivity, "Book not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle any errors that occur during the data retrieval process
                    Toast.makeText(this@UserDetailActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun getLastBBook(userId: String) {
        val borrowRef = FirebaseDatabase.getInstance().getReference("Borrow")
        borrowRef.limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val borrowedBook = childSnapshot.getValue(BorrowedBook::class.java)
                        if (borrowedBook != null && borrowedBook.uID == userId) {
                            bookName.text = borrowedBook.bName
                            borrowedISBN.text = borrowedBook.bIsbn
                        }
                    }
                } else {
                    // Handle the case when there are no borrowed books in the database
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that occur during the data retrieval process
                Toast.makeText(this@UserDetailActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
