package com.example.librarymanagementsystem


import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagementsystem.databinding.ActivityAdminRemoveBookBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore


class AdminRemoveBook : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAdminRemoveBookBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog
    private var b1: Book = Book()

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminRemoveBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.findBook.setOnClickListener(this)
        db = FirebaseFirestore.getInstance()
        FirebaseApp.initializeApp(this)
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        b1 = Book()
    }

    override fun onClick(v: View) {
        progressDialog.setMessage("Please Wait")
        progressDialog.show()
        if (v == binding.findBook) {
            if (binding.editBid1.editText?.text.toString().trim().isEmpty()) {
                progressDialog.cancel()
                binding.editBid1.error = "Book Id Required"
                binding.editBid1.isErrorEnabled = true
                return
            }

            val id = binding.editBid1.editText?.text.toString().trim()
            db.document("Book/$id").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result?.exists() == true) {
                        val alert = AlertDialog.Builder(this)
                        b1 = task.result?.toObject(Book::class.java) ?: Book()
                        val temp =
                            "Title : ${b1.title}\nCategory : ${b1.type}\nNo. of Units : ${b1.total}"
                        progressDialog.cancel()
                        alert.setMessage(temp)
                            .setTitle("Please Confirm !")
                            .setCancelable(false)
                            .setPositiveButton("DELETE") { dialog, which ->
                                dialog.cancel()
                                progressDialog.setMessage("Removing ... ")
                                progressDialog.show()
                                if (b1.available == b1.total) {
                                    db.document("Book/$id")
                                        .delete()
                                        .addOnSuccessListener {
                                            progressDialog.cancel()
                                            Toast.makeText(this, "Book Removed", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                        .addOnFailureListener {
                                            progressDialog.cancel()
                                            Toast.makeText(this, "Try Again !", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                } else {
                                    progressDialog.cancel()
                                    Toast.makeText(
                                        this,
                                        "This Book is issued to Users!\nReturn before Removing this Book.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                            .setNegativeButton("CANCEL") { dialog, which -> dialog.cancel() }
                        val alertDialog = alert.create()
                        alertDialog.show()
                    } else {
                        progressDialog.cancel()
                        Toast.makeText(this, "No such Book found !", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    progressDialog.cancel()
                    Toast.makeText(this, "Try Again !", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
