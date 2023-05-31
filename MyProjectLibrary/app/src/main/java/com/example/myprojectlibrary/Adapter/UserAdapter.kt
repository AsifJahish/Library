package com.example.myprojectlibrary.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.myprojectlibrary.Model.Users
import com.example.myprojectlibrary.R

import com.example.myprojectlibrary.activity.UserDetailActivity

class UserAdapter( private val context: Context, private val usersList: ArrayList<Users>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.userlist, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentBook = usersList[position]
        holder.userid.text = currentBook.userId
        holder.userName.text = currentBook.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, UserDetailActivity::class.java)
            intent.putExtra("Id", currentBook.userId)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userid: TextView = itemView.findViewById(R.id.userid)
        val userName: TextView = itemView.findViewById(R.id.userName)
    }

}