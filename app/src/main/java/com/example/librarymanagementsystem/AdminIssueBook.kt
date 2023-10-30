package com.example.librarymanagementsystem
//
//import android.app.ProgressDialog
//import android.os.Bundle
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.librarymanagementsystem.databinding.ActivityAdminIssueBookBinding
//import com.google.android.gms.tasks.Task
//import com.google.firebase.FirebaseApp
//import com.google.firebase.Timestamp
//import com.google.firebase.firestore.DocumentSnapshot
//import com.google.firebase.firestore.FirebaseFirestore
//
//import java.util.*
//import kotlin.collections.ArrayList
//
//class AdminIssueBook : AppCompatActivity() {
//
//    private lateinit var binding: ActivityAdminIssueBookBinding
//    private lateinit var db: FirebaseFirestore
//    private lateinit var p: ProgressDialog
//    private var res1: Boolean = false
//    private var res2: Boolean = false
//    private var U: User = User()
//    private var B1: Book = Book()
//
//    override fun onBackPressed() {
//        finish()
//        super.onBackPressed()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAdminIssueBookBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        FirebaseApp.initializeApp(this)
//        db = FirebaseFirestore.getInstance()
//        p = ProgressDialog(this)
//
//        binding.issueButton.setOnClickListener {
//            issueBook()
//        }
//    }
//
//    private fun verifyCard(): Boolean {
//        val t = binding.editCardNo1.editText?.text.toString().trim()
//        if (t.isEmpty()) {
//            binding.editCardNo1.error = "Card No. Required"
//            return true
//        }
//        return false
//    }
//
//    private fun verifyBid(): Boolean {
//        val t = binding.editBid3.editText?.text.toString().trim()
//        if (t.isEmpty()) {
//            binding.editBid3.error = "Book Id Required"
//            return true
//        }
//        return false
//    }
//
//    private fun getUser(): Boolean {
//        db.collection("User")
//            .whereEqualTo("card", binding.editCardNo1.editText?.text.toString().toInt())
//            .get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    if (task.result?.size() == 1) {
//                        res1 = true
//                        for (doc in task.result!!) {
//                            U = doc.toObject(User::class.java)
//                        }
//                    } else {
//                        res1 = false
//                        p.cancel()
//                        showToast("No Such User !")
//                    }
//                } else {
//                    res1 = false
//                    p.cancel()
//                    showToast("Try Again !")
//                }
//            }
//        return res1
//    }
//
////    private fun getBook(): Boolean {
////        db.document("Book/${binding.editBid3.editText?.text.toString().toInt() / 100}")
////            .get()
////            .addOnCompleteListener { task: DocumentSnapshot ->
////                if (task.isSuccessful) {
////                    if (task.result?.exists() == true) {
////                        res2 = true
////                        B1 = task.result!!.toObject(Book::class.java) ?: Book()
////                    } else {
////                        res2 = false
////                        p.cancel()
////                        showToast("No Such Book !")
////                    }
////                } else {
////                    res2 = false
////                    p.cancel()
////                    showToast("Try Again !")
////                }
////            }
////        return res2
////    }
//
//    private fun getBook() {
//        db.document("Book/${binding.editBid3.editText?.text.toString().toInt() / 100}")
//            .get()
//            .addOnCompleteListener { task: Task<DocumentSnapshot> ->
//                if (task.isSuccessful) {
//                    val document = task.result
//                    if (document != null && document.exists()) {
//                        B1 = document.toObject(Book::class.java) ?: Book()
//                        issueBook()
//                    } else {
//                        p.cancel()
//                        showToast("No Such Book!")
//                    }
//                } else {
//                    p.cancel()
//                    showToast("Try Again!")
//                }
//            }
//    }
//
//
//
//
//    private fun issueBook() {
//        if (verifyBid() || verifyCard()) {
//            return
//        }
//        p.setMessage("Please Wait !")
//        p.show()
//        if (getBook() && getUser()) {
//            if (U.book.size >= 5) {
//                p.cancel()
//                showToast("User Already Has 5 books issued !")
//                return
//            }
//            if (B1.available == 0) {
//                p.cancel()
//                showToast("No Units of this Book Available !")
//                return
//            }
//            if (B1.unit.contains(binding.editBid3.editText?.text.toString().toInt() % 100)) {
//                p.cancel()
//                showToast("This Unit is Already Issued !")
//                return
//            }
//            val l = U.book.toMutableList()
//            l.add(binding.editBid3.editText?.text.toString().toInt())
//            U.book = l
//            val l2 = U.fine.toMutableList()
//            l2.add(0)
//            U.fine = l2
//            val l3 = U.re.toMutableList()
//            l3.add(1)
//            U.re = l3
//            val l4 = U.date.toMutableList()
//            val c: Calendar = Calendar.getInstance()
//            val d: Date = c.time
//            val t: Timestamp = Timestamp(d)
//            l4.add(t)
//            U.date = l4
//            db.document("User/${U.email}").set(U)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        B1.available--
//                        val l5 = B1.unit.toMutableList()
//                        l5.add(binding.editBid3.editText?.text.toString().toInt() % 100)
//                        B1.unit = l5
//                        db.document("Book/${B1.id}").set(B1)
//                            .addOnCompleteListener { task2 ->
//                                if (task2.isSuccessful) {
//                                    p.cancel()
//                                    showToast("Book Issued Successfully !")
//                                } else {
//                                    p.cancel()
//                                    showToast("Try Again !")
//                                }
//                            }
//                    } else {
//                        p.cancel()
//                        showToast("Try Again !")
//                    }
//                }
//        }
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//}




import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagementsystem.databinding.ActivityAdminIssueBookBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AdminIssueBook : AppCompatActivity() {

    private lateinit var binding: ActivityAdminIssueBookBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var p: ProgressDialog
    private var res1: Boolean = false
    private var res2: Boolean = false
    private var U: User = User()
    private var B1: Book = Book()

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminIssueBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()
        p = ProgressDialog(this)

        binding.issueButton.setOnClickListener {
            issueBook()
        }
    }

    private fun verifyCard(): Boolean {
        val t = binding.editCardNo1.editText?.text.toString().trim()
        return if (t.isEmpty()) {
            binding.editCardNo1.error = "Card No. Required"
            true
        } else {
            binding.editCardNo1.error = null
            false
        }
    }

    private fun verifyBid(): Boolean {
        val t = binding.editBid3.editText?.text.toString().trim()
        return if (t.isEmpty()) {
            binding.editBid3.error = "Book Id Required"
            true
        } else {
            binding.editBid3.error = null
            false
        }
    }

    private fun getUser() {
        db.collection("User")
            .whereEqualTo("card", Integer.parseInt(binding.editCardNo1.editText?.text.toString().trim()))
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result?.size() == 1) {
                        res1 = true
                        for (doc in task.result!!) {
                            U = doc.toObject(User::class.java)
                        }
                    } else {
                        res1 = false
                        p.cancel()
                        Toast.makeText(this, "No Such User !", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    res1 = false
                    p.cancel()
                    Toast.makeText(this, "Try Again !", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun getBook() {
        db.document("Book/" + (Integer.parseInt(binding.editBid3.editText?.text.toString().trim()) / 100))
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result?.exists() == true) {
                        res2 = true
                        B1 = task.result?.toObject(Book::class.java) ?: Book()
                    } else {
                        res2 = false
                        p.cancel()
                        Toast.makeText(this, "No Such Book !", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    res2 = false
                    p.cancel()
                    Toast.makeText(this, "Try Again !", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun issueBook() {
        if (verifyBid() || verifyCard()) {
            return
        }

        p.setMessage("Please Wait !")
        p.show()

        getBook()
        getUser()

        if (res2 && res1) {
            if (U.book.size >= 5) {
                p.cancel()
                Toast.makeText(this, "User Already Has 5 books issued !", Toast.LENGTH_SHORT).show()
                return
            }

            if (B1.available == 0) {
                p.cancel()
                Toast.makeText(this, "No Units of this Book Available !", Toast.LENGTH_SHORT).show()
                return
            }

            if (B1.unit.contains(Integer.parseInt(binding.editBid3.editText?.text.toString().trim()) % 100)) {
                p.cancel()
                Toast.makeText(this, "This Unit is Already Issued !", Toast.LENGTH_SHORT).show()
                return
            }

            val l = U.book.toMutableList()
            l.add(Integer.parseInt(binding.editBid3.editText?.text.toString().trim()))
            U.book = l
            val l2 = U.fine.toMutableList()
            l2.add(0)
            U.fine = l2
            val l3 = U.re.toMutableList()
            l3.add(1)
            U.re = l3
            val l4 = U.date.toMutableList()
            val c = Calendar.getInstance()
            val d = c.time
            val t = Timestamp(d)
            l4.add(t)
            U.date = l4

            db.document("User/${U.email}")
                .set(U)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        B1.available -= 1
                        val l5 = B1.unit.toMutableList()
                        l5.add(Integer.parseInt(binding.editBid3.editText?.text.toString().trim()) % 100)
                        B1.unit = l5

                        db.document("Book/${B1.id}")
                            .set(B1)
                            .addOnCompleteListener { task2 ->
                                if (task2.isSuccessful) {
                                    p.cancel()
                                    Toast.makeText(this, "Book Issued Successfully !", Toast.LENGTH_SHORT).show()
                                } else {
                                    p.cancel()
                                    Toast.makeText(this, "Try Again !", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        p.cancel()
                        Toast.makeText(this, "Try Again !", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
