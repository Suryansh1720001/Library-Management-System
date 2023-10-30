package com.example.librarymanagementsystem

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagementsystem.databinding.ActivityAdminReissueBookBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class AdminReissueBook : AppCompatActivity() {

    private lateinit var binding: ActivityAdminReissueBookBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var p: ProgressDialog
    private var res1: Boolean = false
    private var U: User = User()

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminReissueBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()
        p = ProgressDialog(this)
        p.setMessage("Please Wait !")

        binding.reissueButton.setOnClickListener {
            reissueBook()
        }
    }

    private fun verifyCard(): Boolean {
        val t = binding.editCardNo5.editText?.text.toString().trim()
        if (t.isEmpty()) {
            binding.editCardNo5.error = "Card No. Required"
            return true
        }
        return false
    }

    private fun verifyBid(): Boolean {
        val t = binding.editBid5.editText?.text.toString().trim()
        if (t.isEmpty()) {
            binding.editBid5.error = "Book Id Required"
            return true
        }
        return false
    }

    private fun getUser() {
        db.collection("User")
            .whereEqualTo("card", binding.editCardNo5.editText?.text.toString().toInt())
            .get()
            .addOnCompleteListener { task: Task<QuerySnapshot> ->
                if (task.isSuccessful && task.result?.size() == 1) {
                    res1 = true
                    for (doc: QueryDocumentSnapshot in task.result!!) {
                        U = doc.toObject(User::class.java) ?: User()
                    }
                } else if (task.isSuccessful && task.result?.size() != 1) {
                    res1 = false
                    p.cancel()
                    showToast("No Such User !")
                } else {
                    res1 = false
                    p.cancel()
                    showToast("Try Again !")
                }
            }
    }

    private fun reissueBook() {
        if (verifyBid() || verifyCard()) {
            return
        }
        p.show()
        getUser()
        if (!res1) {
            return
        }
        if (!U.book.contains(binding.editBid5.editText?.text.toString().toInt())) {
            p.cancel()
            showToast("This Book is not issued to this User !")
            return
        }
        val l: MutableList<Int> = U.book.toMutableList()
        val i: Int = l.indexOf(binding.editBid5.editText?.text.toString().toInt())
        U.leftFine += U.fine[i]
        val l2: MutableList<Int> = U.fine.toMutableList()
        l2[i] = 0
        U.fine = l2
        val l3: MutableList<Int> = U.re.toMutableList()
        l3[i] = 1
        U.re = l3
        val l4: MutableList<Timestamp> = U.date.toMutableList()
        val c: Calendar = Calendar.getInstance()
        val d: Date = c.time
        val t: Timestamp = Timestamp(d)
        l4[i] = t
        U.date = l4
        db.document("User/${U.email}").set(U)
            .addOnCompleteListener { task: Task<Void> ->
                if (task.isSuccessful) {
                    p.cancel()
                    showToast("Re-Issued Successfully !")
                } else {
                    p.cancel()
                    showToast("Please try Again !")
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

