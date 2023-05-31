package com.example.myprojectlibrary.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myprojectlibrary.Adapter.BookAdapters
import com.example.myprojectlibrary.Model.Books
import com.example.myprojectlibrary.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Home : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbref: DatabaseReference
    private lateinit var bookList: ArrayList<Books>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view= inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recycleview)
        recyclerView.layoutManager = LinearLayoutManager(context)


        // Initialize other variables if needed
        bookList = arrayListOf<Books>()

        getBooksData()
        return view
    }

    private fun getBooksData(){
        recyclerView.visibility = View.GONE

        dbref = FirebaseDatabase.getInstance().getReference("books")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookList.clear()
                if (snapshot.exists()) {
                    for (booksnap in snapshot.children) {
                        val bookData = booksnap.getValue(Books::class.java)
                        bookData?.let {
                            bookList.add(it)
                        }
                    }
                    val myAdapter = context?.let { BookAdapters(it, bookList) }
                    recyclerView.adapter = myAdapter
                    recyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the cancellation if needed
            }
        })
    }

}