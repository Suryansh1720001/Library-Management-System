package com.example.librarymanagementsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams

import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class BookAdapter(options: FirestoreRecyclerOptions<com.example.librarymanagementsystem.Book>, private val key: String, private val mode: Int) :
    FirestoreRecyclerAdapter<Book, BookAdapter.Book>(options) {

    class Book(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookName: TextView = itemView.findViewById(R.id.bookName)
        val bookId: TextView = itemView.findViewById(R.id.bookId)
        val bookType: TextView = itemView.findViewById(R.id.bookType)
        val bookAvailable: TextView = itemView.findViewById(R.id.bookAvailable)
        val bookTotal: TextView = itemView.findViewById(R.id.bookTotal)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Book {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.book_view, viewGroup, false)
        return Book(view)
    }

    override fun onBindViewHolder(
        holder: Book,
        position: Int,
        model: com.example.librarymanagementsystem.Book,
    ) {
        holder.bookId.text = "ID: ${model.id}"
        holder.bookType.text = "Category: ${model.type}"
        holder.bookAvailable.text = "Available: ${model.available}"
        holder.bookName.text = "Title: ${model.title}"
        holder.bookTotal.text = "Total: ${model.total}"

        if (!model.title.contains(key) && mode == 1) {
            holder.itemView.visibility = View.GONE
            val layoutParams = holder.itemView.layoutParams as LayoutParams
            layoutParams.height = 0
            layoutParams.width = 0
            holder.itemView.layoutParams = layoutParams
        }
    }
}
