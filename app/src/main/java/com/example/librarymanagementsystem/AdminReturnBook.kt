package com.example.librarymanagementsystem

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class AdminReturnBook : AppCompatActivity() {
    private lateinit var returnButton: Button
    private lateinit var editCardNo2: TextInputLayout
    private lateinit var editBid4: TextInputLayout
    private lateinit var db: FirebaseFirestore
    private lateinit var p: ProgressDialog
    private var res1 = false
    private var res2 = false
    private var U = User()
    private var B = Book()

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_return_book)
        FirebaseApp.initializeApp(this)
        returnButton = findViewById(R.id.returnButton)
        editBid4 = findViewById(R.id.editBid4)
        editCardNo2 = findViewById(R.id.editCardNo2)
        db = FirebaseFirestore.getInstance()
        p = ProgressDialog(this)
        returnButton.setOnClickListener {
            returnBook()
        }
    }

    private fun verifyCard(): Boolean {
        val t = editCardNo2.editText?.text.toString().trim()
        return if (t.isEmpty()) {
            editCardNo2.error = "Card No. Required"
            true
        } else {
            editCardNo2.error = null
            false
        }
    }

    private fun verifyBid(): Boolean {
        val t = editBid4.editText?.text.toString().trim()
        return if (t.isEmpty()) {
            editBid4.error = "Book Id Required"
            true
        } else {
            editBid4.error = null
            false
        }
    }

    private fun getUser(): Boolean {
        db.collection("User")
            .whereEqualTo("card", editCardNo2.editText?.text.toString().trim().toInt())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.size() == 1) {
                        res1 = true
                        for (doc in task.result) {
                            U = doc.toObject(User::class.java)
                        }
                    } else {
                        res1 = false
                        p.cancel()
                        Toast.makeText(this, "No Such User!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    res1 = false
                    p.cancel()
                    Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show()
                }
            }
        return res1
    }

    private fun getBook(): Boolean {
        db.document("Book/${editBid4.editText?.text.toString().toInt() / 100}")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.exists()) {
                        res2 = true
                        B = task.result.toObject(Book::class.java) ?: Book()
                    } else {
                        res2 = false
                        p.cancel()
                        Toast.makeText(this, "No Such Book!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    res2 = false
                    p.cancel()
                    Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show()
                }
            }
        return res2
    }



    private fun returnBook() {
        if (verifyBid() || verifyCard()) return

        p.setMessage("Please Wait!")
        p.show()

        // Check if both user and book data retrieval is successful
        if (getUser() && getBook()) {
            // Check if the user has the book
            if (!U.book.contains(editBid4.editText?.text.toString().toInt())) {
                p.cancel()
                Toast.makeText(this, "Given Book is not issued to the User!", Toast.LENGTH_SHORT).show()
                return
            }

            val bookIndex = U.book.indexOf(editBid4.editText?.text.toString().toInt())

            // Create new lists without the book information
            val newBooks = U.book.toMutableList()
            val newFine = U.fine.toMutableList()
            val newRe = U.re.toMutableList()
            val newDate = U.date.toMutableList()

            newBooks.removeAt(bookIndex)
            newFine.removeAt(bookIndex)
            newRe.removeAt(bookIndex)
            newDate.removeAt(bookIndex)

            // Update the user's data with the new lists
            U.book = newBooks
            U.fine = newFine
            U.re = newRe
            U.date = newDate

            // Update the user's data in Firestore
            db.document("User/${U.email}").set(U)
                .addOnCompleteListener { userUpdateTask ->
                    if (userUpdateTask.isSuccessful) {
                        // Book information successfully removed from the user
                        val unitIndex = B.unit.indexOf(editBid4.editText?.text.toString().toInt() % 100)
                        B.available++
                        B.unit.removeAt(unitIndex)

                        // Update the book data in Firestore
                        db.document("Book/${B.id}").set(B)
                            .addOnCompleteListener { bookUpdateTask ->
                                if (bookUpdateTask.isSuccessful) {
                                    p.cancel()
                                    Toast.makeText(this, "Book Returned Successfully!", Toast.LENGTH_SHORT).show()
                                } else {
                                    p.cancel()
                                    Toast.makeText(this, "Error updating book data. Try Again!", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        p.cancel()
                        Toast.makeText(this, "Error updating user data. Try Again!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }



}
