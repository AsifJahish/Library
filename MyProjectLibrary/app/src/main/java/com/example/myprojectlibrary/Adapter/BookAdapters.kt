package com.example.myprojectlibrary.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myprojectlibrary.Model.Books
import com.example.myprojectlibrary.R
import com.example.myprojectlibrary.activity.DetailsActivity

class BookAdapters(private val context: Context, private val booksList: ArrayList<Books>): RecyclerView.Adapter<BookAdapters.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.list_book, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentBook= booksList[position]
        holder.titleB.text= currentBook.title
        holder.authorB.text= currentBook.author
        holder.generaB.text= currentBook.category
        holder.publishB.text= currentBook.publisher

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("isbn", currentBook.isbn)
            context.startActivity(intent)
        }



    }

    override fun getItemCount(): Int {
        return booksList.size
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val titleB: TextView = itemView.findViewById(R.id.title)
        val authorB: TextView = itemView.findViewById(R.id.author)
        val generaB: TextView = itemView.findViewById(R.id.genera)
        val publishB: TextView = itemView.findViewById(R.id.publish)
    }

}