package com.example.librarymanagementsystem

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagementsystem.databinding.ActivityAdminCollectFineBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore


class AdminCollectFine : AppCompatActivity() {

    private lateinit var binding: ActivityAdminCollectFineBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var db: FirebaseFirestore
    private lateinit var U: User

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun collectFine() {
        var tot = U.leftFine
        for (fine in U.fine) {
            tot += fine
        }

        if (tot == 0) {
            Toast.makeText(this, "This User has no Fine !", Toast.LENGTH_SHORT).show()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Collect Fine !")
                .setMessage("Collect Rs.$tot from ${U.name}")
                .setPositiveButton("Collect") { dialog, _ ->
                    dialog.cancel()
                    progressDialog.show()
                    val list = U.fine.toMutableList()
                    for (i in list.indices) {
                        list[i] = 0
                    }
                    U.fine = list
                    U.leftFine= 0
                    db.document("User/${U.email}").set(U)
                        .addOnCompleteListener(OnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@AdminCollectFine, "Fine Collected !", Toast.LENGTH_SHORT).show()
                                progressDialog.cancel()
                            } else {
                                Toast.makeText(this@AdminCollectFine, "Try Again !", Toast.LENGTH_SHORT).show()
                                progressDialog.cancel()
                            }
                        })
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                .create()
                .show()
        }
    }

    private fun verifyUser(): Boolean {
        val t = binding.editUser.editText?.text.toString().trim()
        return if (t.isEmpty()) {
            binding.editUser.error = "Card No. Required"
            true
        } else {
            binding.editUser.error = null
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminCollectFineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()
        progressDialog.setMessage("Please Wait !")

        binding.collect.setOnClickListener(View.OnClickListener {
            progressDialog.show()

            if (verifyUser()) return@OnClickListener

            val card = binding.editUser.editText?.text.toString().trim().toInt()
            db.collection("User")
                .whereEqualTo("card", card)
                .get()
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (!task.result?.isEmpty()!!) {
                            for (x in task.result!!) {
                                U = x.toObject(User::class.java)
                            }
                            progressDialog.cancel()
                            collectFine()
                        } else {
                            progressDialog.cancel()
                            Toast.makeText(this@AdminCollectFine, "No Such User !", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        progressDialog.cancel()
                        Toast.makeText(this@AdminCollectFine, "Try Again !", Toast.LENGTH_SHORT).show()
                    }
                })
        })
    }
}
