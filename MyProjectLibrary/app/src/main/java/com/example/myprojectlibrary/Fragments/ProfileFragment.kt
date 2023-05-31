package com.example.myprojectlibrary.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myprojectlibrary.Adapter.BorrowedAdapter
import com.example.myprojectlibrary.Adapter.UserAdapter
import com.example.myprojectlibrary.Model.BorrowedBook
import com.example.myprojectlibrary.Model.Users
import com.example.myprojectlibrary.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    private lateinit var dbref: DatabaseReference
    private lateinit var userEmail: TextView
    private lateinit var userName: TextView
    private lateinit var lastBook: TextView
    private lateinit var lastBookISBN: TextView
    private lateinit var BorrowedList: ArrayList<BorrowedBook>
    private lateinit var recyclerViewProfile: RecyclerView

    companion object {
        fun newInstance(userId: String?): ProfileFragment {
            val fragment = ProfileFragment()
            val bundle = Bundle()
            bundle.putString("userId", userId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        userEmail = view.findViewById(R.id.emailP)
        userName = view.findViewById(R.id.nameP)
        lastBook = view.findViewById(R.id.booknameP)
        lastBookISBN = view.findViewById(R.id.lastISBNP)
        recyclerViewProfile = view.findViewById(R.id.recycleviewProfile)

        recyclerViewProfile.layoutManager = LinearLayoutManager(requireContext())

        BorrowedList = arrayListOf<BorrowedBook>()

        val userId = arguments?.getString("userId")
        if (userId != null) {
            getUData(userId)
            getLastBBook(userId)
            getPastBook(userId)
        }




        return view
    }

    private fun getUData(userId: String?) {
        if (userId.isNullOrEmpty()) {
            return
        }

        dbref = FirebaseDatabase.getInstance().getReference("users").child(userId)
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(Users::class.java)
                    if (user != null) {
                        requireActivity().runOnUiThread {
                            userName.text = user.name
                            userEmail.text = user.email
                        }
                    }
                } else {
                    // Handle the case when the user data does not exist in the database
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun getLastBBook(userId: String?) {
        if (userId.isNullOrEmpty()) {
            return
        }

        dbref = FirebaseDatabase.getInstance().getReference("Borrow")
        dbref.orderByChild("uid").equalTo(userId).limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (bookSnapshot in snapshot.children) {
                            val borrowedBook = bookSnapshot.getValue(BorrowedBook::class.java)
                            if (borrowedBook != null) {
                                requireActivity().runOnUiThread {
                                    lastBook.text = borrowedBook.bName
                                    lastBookISBN.text = borrowedBook.bIsbn
                                }
                            }
                        }
                    } else {
                        // Handle the case when no borrowed books exist for the user
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getPastBook(userId: String?) {
        if (userId.isNullOrEmpty()) {
            return
        }

        dbref = FirebaseDatabase.getInstance().getReference("Borrow")
        dbref.orderByChild("uid").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    BorrowedList.clear()
                    if (snapshot.exists()) {
                        for (usersnap in snapshot.children) {
                            val userData = usersnap.getValue(BorrowedBook::class.java)
                            userData?.let {
                                BorrowedList.add(it)
                            }
                        }
                        val userAdapter = BorrowedAdapter(requireContext(), BorrowedList)
                        recyclerViewProfile.adapter = userAdapter
                        recyclerViewProfile.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the cancellation if needed
                }
            })
    }


}
