package com.example.librarymanagementsystem

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagementsystem.databinding.ActivityAdminUpdateBookBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class AdminUpdateBook : AppCompatActivity() {
    private lateinit var binding: ActivityAdminUpdateBookBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var p1: ProgressDialog
    private val book = Book()
    private var qtity: Int = 0
    private var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUpdateBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()
        p1 = ProgressDialog(this)
        p1.setCancelable(false)

        val A = resources.getStringArray(R.array.list1)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, A)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner2.adapter = adapter
        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        binding.button2.setOnClickListener {
            updateBook()
        }
    }

    private fun verifyTitle(): Boolean {
        val t = binding.editTitle2.editText?.text.toString().trim()
        return !t.isEmpty()
    }

    private fun verifyBid(): Boolean {
        val b = binding.editBid2.editText?.text.toString().trim()
        if (b.isEmpty()) {
            binding.editBid2.error = "Book ID Required"
            return true
        }
        binding.editBid2.error = null
        return false
    }

    private fun verifyUnits(): Boolean {
        val u = binding.editUnits2.editText?.text.toString().trim()
        return !u.isEmpty()
    }

    private fun verifyCategory(): Boolean {
        return type != "Select Book Category"
    }

    private fun updateBook() {
        if (verifyBid()) return

        if (!(verifyCategory() || verifyTitle() || verifyUnits())) {
            Toast.makeText(this, "Select something to Update !", Toast.LENGTH_SHORT).show()
            return
        }

        p1.setMessage("Updating ...")
        p1.show()

        db.document("Book/${binding.editBid2.editText?.text.toString().toInt()}")
            .get()
            .addOnCompleteListener { task: Task<DocumentSnapshot> ->
                if (task.isSuccessful) {
                    if (task.result?.exists() == true) {
                        book.apply {
                            if (verifyCategory()) {
                                type = this@AdminUpdateBook.type
                            }

                            if (verifyUnits()) {
                                val temp1 = total
                                total = binding.editUnits2.editText?.text.toString().toInt()
                                qtity = available - temp1 + total
                                available = qtity
                            }
                            if (verifyTitle()) {
                                title = binding.editTitle2.editText?.text.toString().toUpperCase()
                            }
                        }

                        if (qtity >= 0) {
                            db.document("Book/${binding.editBid2.editText?.text.toString().toInt()}")
                                .set(book)
                                .addOnCompleteListener { task: Task<Void> ->
                                    if (task.isSuccessful) {
                                        p1.cancel()
                                        Toast.makeText(this, "Updated Successfully !", Toast.LENGTH_SHORT)
                                            .show()
                                    } else {
                                        p1.cancel()
                                        Toast.makeText(this, "Try Again !", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            p1.cancel()
                            Toast.makeText(this, "Can't Reduce No. of Units \ndue to issued units !", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        p1.cancel()
                        Toast.makeText(this, "No Such Book !", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    p1.cancel()
                    Toast.makeText(this, "Try Again !", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
