package com.example.librarymanagementsystem


import android.app.ProgressDialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.librarymanagementsystem.databinding.ActivitySearchBookBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class SearchBook : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: FirestoreRecyclerAdapter<*, *>
    private var mode = 0
    private var key = ""
    private lateinit var progressDialog: ProgressDialog
    private lateinit var ifNoBook: TextView
    private lateinit var query: Query

    private lateinit var binding: ActivitySearchBookBinding

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ifNoBook = binding.ifNoBook
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please Wait !")
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()
        progressDialog.show()

        val intent = intent

        when (intent.getIntExtra("id", 0)) {
            1 -> {
                mode = 0
                query = db.collection("Book")
                    .whereEqualTo("id", intent.getIntExtra("bid", 0))
                    .whereGreaterThan("available", 0)
            }
            2 -> {
                mode = 0
                query = db.collection("Book")
                    .whereEqualTo("id", intent.getIntExtra("bid", 0))
            }
            3 -> {
                mode = 1
                key = intent.getStringExtra("btitle") ?: ""
                query = db.collection("Book")
                    .whereEqualTo("type", intent.getStringExtra("btype"))
                    .whereGreaterThan("available", 0)
            }
            4 -> {
                mode = 1
                key = intent.getStringExtra("btitle") ?: ""
                query = db.collection("Book")
                    .whereEqualTo("type", intent.getStringExtra("btype"))
            }
            5 -> {
                mode = 1
                key = intent.getStringExtra("btitle") ?: ""
                query = db.collection("Book")
                    .whereGreaterThan("available", 0)
            }
            6 -> {
                mode = 1
                key = intent.getStringExtra("btitle") ?: ""
                query = db.collection("Book")
            }
            7 -> {
                mode = 0
                query = db.collection("Book")
                    .whereEqualTo("type", intent.getStringExtra("btype"))
                    .whereGreaterThan("available", 0)
            }
            8 -> {
                mode = 0
                query = db.collection("Book")
                    .whereEqualTo("type", intent.getStringExtra("btype"))
            }
        }

        val options = FirestoreRecyclerOptions.Builder<Book>()
            .setQuery(query, Book::class.java)
            .build()
        adapter = BookAdapter(options, key.toUpperCase(), mode)
        val recyclerView = binding.recycle
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.startListening()
        progressDialog.cancel()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}
