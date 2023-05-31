package com.example.myprojectlibrary.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.myprojectlibrary.Adapter.UserAdapter
import com.example.myprojectlibrary.Model.Users
import com.example.myprojectlibrary.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UserListFragment : Fragment() {
    private lateinit var recyclerViewUsers: RecyclerView
    private lateinit var dbref: DatabaseReference
    private lateinit var usersList: ArrayList<Users>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_user_list, container, false)


        recyclerViewUsers = view.findViewById(R.id.recycleviewUser)
        recyclerViewUsers.layoutManager = LinearLayoutManager(context)


        // Initialize other variables if needed
        usersList = arrayListOf<Users>()

        getUserData()

        return view
    }



    private fun getUserData(){

        dbref = FirebaseDatabase.getInstance().getReference("users")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                if (snapshot.exists()) {
                    for (usersnap in snapshot.children) {
                        val userData = usersnap.getValue(Users::class.java)
                        userData?.let {
                            usersList.add(it)
                        }
                    }
                    val userAdapter = context?.let { UserAdapter(it, usersList) }
                    recyclerViewUsers.adapter = userAdapter
                    recyclerViewUsers.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the cancellation if needed
            }
        })
    }

}