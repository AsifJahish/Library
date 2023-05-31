package com.example.myprojectlibrary.Adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myprojectlibrary.Model.BorrowedBook

import com.example.myprojectlibrary.R


class BorrowedAdapter( private val context: Context, private val BorrowedList: ArrayList<BorrowedBook>):
    RecyclerView.Adapter<BorrowedAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.pastborrow, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentBook = BorrowedList[position]
        holder.titlePAST.text = currentBook.bName
        holder.ISBNPAST.text = currentBook.bIsbn
/*
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("ISBN", currentBook.)
            context.startActivity(intent)
        }*/


    }

    override fun getItemCount(): Int {
        return BorrowedList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titlePAST: TextView = itemView.findViewById(R.id.titlePAST)
        val ISBNPAST: TextView = itemView.findViewById(R.id.isbnPAST)
    }

}