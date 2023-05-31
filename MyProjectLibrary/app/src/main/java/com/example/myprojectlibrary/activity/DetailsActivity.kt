package com.example.myprojectlibrary.activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myprojectlibrary.Model.Books
import com.example.myprojectlibrary.R
import com.example.myprojectlibrary.databinding.ActivityDetailsBinding
import com.google.firebase.database.*

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().reference

        // Retrieve the extra input from the intent
        val bookId = intent.getStringExtra("isbn")
        if (bookId.isNullOrEmpty()) {
            // Handle the case when bookId is null or empty
            return
        }

        // Query the database to retrieve the book details
        database.child("books").child(bookId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val book = snapshot.getValue(Books::class.java)
                    if (book != null) {
                        // Update the views with the retrieved book details
                        binding.authorD.text = "Author : \n"+book.author
                        binding.categoryD.text = "category :\n "+book.category
                        binding.titleD.text = "title :\n"+ book.title
                        binding.conditionD.text = "Book Condition : \n"+book.condition
                        binding.covertypeD.text = "cover Type : \n"+book.coverType
                        binding.editionD.text = "Book Edition: \n "+book.edition
                        binding.isbnD.text = "ISBN :\n"+book.isbn
                        binding.publisherD.text = "Publisher :\n"+ book.publisher
                        binding.languageD.text = "Book Language : \n"+book.language
                        binding.placeD.text = "Place of Publication :\n"+book.place
                        binding.editorD.text ="Editior :\n"+ book.editor
                        binding.TranslatorD.text ="Transaltor: \n"+ book.translator
                        binding.shelf.text = "shelf Location : \n"+book.shelf
                        binding.notes.text = "Notes :\n"+book.notes
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}
