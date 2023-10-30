package com.example.librarymanagementsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

class MyBookAdapter(private val myBooks: List<MyBook>) :
    RecyclerView.Adapter<MyBookAdapter.MyBookView>() {

    private val simpleDateFormat = SimpleDateFormat("dd/MM/yy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBookView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.see_my_book_view, parent, false)
        return MyBookView(view)
    }

    override fun onBindViewHolder(holder: MyBookView, position: Int) {
        val myBook = myBooks[position]
        holder.bookId1.text = "ID : ${myBook.Bid}"
        holder.bookName1.text = "Title : ${myBook.Title}"
        holder.bookType1.text = "Type : ${myBook.Type}"
        holder.bookIdate.text = "Issue Date : ${simpleDateFormat.format(myBook.Idate)}"
        holder.bookDdate.text = "Due Date : ${simpleDateFormat.format(myBook.Ddate)}"
    }

    override fun getItemCount(): Int {
        return myBooks.size
    }

    class MyBookView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookId1: TextView = itemView.findViewById(R.id.bookId1)
        val bookIdate: TextView = itemView.findViewById(R.id.bookIdate)
        val bookName1: TextView = itemView.findViewById(R.id.bookName1)
        val bookType1: TextView = itemView.findViewById(R.id.bookType1)
        val bookDdate: TextView = itemView.findViewById(R.id.bookDdate)
    }
}
