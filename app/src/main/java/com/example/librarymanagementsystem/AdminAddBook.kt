package com.example.librarymanagementsystem


import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagementsystem.databinding.ActivityAdminAddBookBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class AdminAddBook : AppCompatActivity() {

    private lateinit var binding: ActivityAdminAddBookBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var p1: ProgressDialog
    private var type: String = ""

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddBookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()

        val A = resources.getStringArray(R.array.list1)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, A)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner1.adapter = adapter

        binding.spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.button1.setOnClickListener { addBook() }
    }

    private fun verifyTitle(): Boolean {
        val t = binding.editTitle.editText?.text.toString().trim()
        return if (t.isEmpty()) {
            binding.editTitle.isErrorEnabled = true
            binding.editTitle.error = "Title Required"
            true
        } else {
            binding.editTitle.isErrorEnabled = false
            false
        }
    }

    private fun verifyBid(): Boolean {
        val b = binding.editBid.editText?.text.toString().trim()
        return if (b.isEmpty()) {
            binding.editBid.isErrorEnabled = true
            binding.editBid.error = "Book Id Required"
            true
        } else {
            binding.editBid.isErrorEnabled = false
            false
        }
    }

    private fun verifyUnits(): Boolean {
        val u = binding.editUnits.editText?.text.toString().trim()
        return if (u.isEmpty()) {
            binding.editUnits.isErrorEnabled = true
            binding.editUnits.error = "No. of Units Required"
            true
        } else {
            binding.editUnits.isErrorEnabled = false
            false
        }
    }

    private fun verifyCategory(): Boolean {
        return if (type == "Select Book Category") {
            Toast.makeText(this, "Please select Book Category !", Toast.LENGTH_SHORT).show()
            true
        } else {
            false
        }
    }

    private fun addBook() {
        val res = verifyBid() || verifyTitle() || verifyUnits() || verifyCategory()
        if (res) return

        p1 = ProgressDialog(this)
        p1.setCancelable(false)
        p1.setMessage("Adding Book")
        p1.show()

        val id = binding.editBid.editText?.text.toString().trim()
        val id1 = id.toInt()

        db.document("Book/$id1").get().addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful && !task.result.exists()) {
                val title = binding.editTitle.editText?.text.toString().trim()
                val units = binding.editUnits.editText?.text.toString().trim()
                val id1 = id.toInt()
                val unit1 = units.toInt()
                val b = Book(title.toUpperCase(), type, unit1, id1)
                db.document("Book/$id1").set(b).addOnCompleteListener { task2 ->
                    if (task2.isSuccessful) {
                        p1.dismiss()
                        Toast.makeText(this, "Book Added !", Toast.LENGTH_SHORT).show()
                    } else {
                        p1.dismiss()
                        Toast.makeText(this, "Try Again !", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                p1.dismiss()
                Toast.makeText(this, "This Book is already added\nor Bad Connection !", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
