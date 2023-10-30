package com.example.librarymanagementsystem

//
//import android.app.ProgressDialog
//import android.os.Bundle
//import android.view.View
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.librarymanagementsystem.databinding.ActivityUserReissueBookBinding
//import com.google.android.gms.tasks.OnCompleteListener
//import com.google.android.gms.tasks.Task
//import com.google.firebase.FirebaseApp
//import com.google.firebase.Timestamp
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.DocumentSnapshot
//import com.google.firebase.firestore.FirebaseFirestore
//import java.util.*
//
//class UserReissueBook : AppCompatActivity() {
//    private lateinit var binding: ActivityUserReissueBookBinding
//    private lateinit var db: FirebaseFirestore
//    private lateinit var firebaseAuth: FirebaseAuth
//    private lateinit var progressDialog: ProgressDialog
//    private var res1 = false
//    private val user = User()
//    private var flag: String? = null
//    private val books = mutableListOf<String>()
//
//    override fun onBackPressed() {
//        finish()
//        super.onBackPressed()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityUserReissueBookBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        FirebaseApp.initializeApp(this)
//        books.add("Select Book")
//
//        binding.reissueButton2.setOnClickListener {
//            reissueBook()
//        }
//
//        binding.spinner4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                flag = parent?.getItemAtPosition(position).toString()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//        }
//
//        db = FirebaseFirestore.getInstance()
//        firebaseAuth = FirebaseAuth.getInstance()
//        progressDialog = ProgressDialog(this)
//        progressDialog.setMessage("Please Wait !")
//        progressDialog.show()
//
//        db.document("User/" + firebaseAuth.currentUser?.email)
//            .get()
//            .addOnCompleteListener(OnCompleteListener { task: Task<DocumentSnapshot> ->
//                if (task.isSuccessful) {
//                    progressDialog.cancel()
//                    val result = task.result.toObject(User::class.java)
//                    if (result != null) {
//                        user.copyFrom(result)
//                        for (i in 0 until user.book.size) {
//                            if (user.re[i] == 1) {
//                                books.add(user.book[i].toString())
//                            }
//                        }
//                    }
//                } else {
//                    progressDialog.cancel()
//                }
//            })
//
//        setSpinner4()
//    }
//
//    private fun setSpinner4() {
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, books)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.spinner4.adapter = adapter
//    }
//
//    private fun verifyCategory(): Boolean {
//        if (flag == "Select Book") {
//            Toast.makeText(this, "Please select a book to Re-issue !", Toast.LENGTH_SHORT).show()
//            return true
//        }
//        return false
//    }
//
//    private fun reissueBook() {
//        if (verifyCategory()) return
//        progressDialog.show()
//        val l = ArrayList<Int>()
//        val i = user.book.indexOf(flag?.toInt())
//        user.leftFine += user.fine[i]
//        l.addAll(user.fine)
//        l[i] = 0
//        user.fine = l
//        l.clear()
//        l.addAll(user.re)
//        l[i] = 0
//        user.re = l
//        val l1 = ArrayList<Timestamp>()
//        l1.addAll(user.date)
//        val c = Calendar.getInstance()
//        val d = c.time
//        val t = Timestamp(d)
//        l1[i] = t
//        user.date = l1
//        db.document("User/" + user.email).set(user)
//            .addOnCompleteListener { task: Task<Void?> ->
//                if (task.isSuccessful) {
//                    progressDialog.cancel()
//                    Toast.makeText(this, "Re-Issued Successfully !", Toast.LENGTH_SHORT).show()
//                } else {
//                    progressDialog.cancel()
//                    Toast.makeText(this, "Please try Again !", Toast.LENGTH_SHORT).show()
//                }
//            }
//        books.remove(flag)
//        setSpinner4()
//        progressDialog.cancel()
//    }
//}




import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagementsystem.databinding.ActivityUserReissueBookBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

import java.util.*

class UserReissueBook : AppCompatActivity() {
    private lateinit var binding: ActivityUserReissueBookBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var res1 = false
    private val user = User()
    private var flag: String? = null
    private val books = mutableListOf<String>()

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserReissueBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        books.add("Select Book")

        binding.reissueButton2.setOnClickListener {
            reissueBook()
        }

        binding.spinner4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                flag = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please Wait !")
        progressDialog.show()

        db.document("User/" + firebaseAuth.currentUser?.email)
            .get()
            .addOnCompleteListener(OnCompleteListener { task: Task<DocumentSnapshot> ->
                if (task.isSuccessful) {
                    progressDialog.cancel()
                    val result = task.result.toObject(User::class.java)
                    if (result != null) {
                        user.email = result.email
                        user.book = result.book
                        user.fine = result.fine
                        user.re = result.re
                        user.leftFine = result.leftFine
                        user.date = result.date
                        for (i in 0 until user.book.size) {
                            if (user.re[i] == 1) {
                                books.add(user.book[i].toString())
                            }
                        }
                    }
                } else {
                    progressDialog.cancel()
                }
            })

        setSpinner4()
    }

    private fun setSpinner4() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, books)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner4.adapter = adapter
    }

    private fun verifyCategory(): Boolean {
        if (flag == "Select Book") {
            Toast.makeText(this, "Please select a book to Re-issue !", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    private fun reissueBook() {
        if (verifyCategory()) return
        progressDialog.show()
        val l = ArrayList<Int>()
        val i = user.book.indexOf(flag?.toInt())
        user.leftFine += user.fine[i]
        l.addAll(user.fine)
        l[i] = 0
        user.fine = l
        l.clear()
        l.addAll(user.re)
        l[i] = 0
        user.re = l
        val l1 = ArrayList<Timestamp>()
        l1.addAll(user.date)
        val c = Calendar.getInstance()
        val d = c.time
        val t = Timestamp(d)
        l1[i] = t
        user.date = l1
        db.document("User/" + user.email).set(user)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    progressDialog.cancel()
                    Toast.makeText(this, "Re-Issued Successfully !", Toast.LENGTH_SHORT).show()
                } else {
                    progressDialog.cancel()
                    Toast.makeText(this, "Please try Again !", Toast.LENGTH_SHORT).show()
                }
            }
        books.remove(flag)
        setSpinner4()
        progressDialog.cancel()
    }
}
