package com.example.librarymanagementsystem

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagementsystem.databinding.ActivitySigninBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        FirebaseApp.initializeApp(applicationContext)
        db = FirebaseFirestore.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.buttonSignIn.setOnClickListener {
            signinUser()
        }

        binding.toSignUp.setOnClickListener {
//            startActivity(Intent(applicationContext, SignUpActivity::class.java))
            startActivity(Intent(this@SignInActivity,SignUpActivity::class.java))
            finish()
        }

        if (firebaseAuth.currentUser != null) {
            progressDialog.setMessage("Please Wait... Signing You in!")
            progressDialog.show()
            val cur = firebaseAuth.currentUser?.email?.trim()
            db.document("User/$cur").get().addOnSuccessListener(OnSuccessListener { documentSnapshot ->
                val obj = documentSnapshot.toObject(User::class.java)
                if (obj?.type == 0) {
                    progressDialog.cancel()
                    startActivity(Intent(applicationContext, UserHome::class.java))
                    finish()
                } else {
                    progressDialog.cancel()
                    startActivity(Intent(applicationContext, AdminHome::class.java))
                    finish()
                }
            }).addOnFailureListener(OnFailureListener {
                progressDialog.cancel()
                Toast.makeText(this@SignInActivity, "Please Sign in Again", Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun verifyEmailId(): Boolean {

        val emailId = binding.editID.editText?.text.toString().trim()
        return if (emailId.isEmpty()) {
            binding.editID.error = "Email ID Required"
            true
        } else {
            binding.editID.isErrorEnabled = false
            false
        }
    }

    private fun verifyPass(): Boolean {
        val pass = binding.editPass.editText?.text.toString().trim()
        return if (pass.isEmpty()) {
            binding.editPass.error = "Password Required"
            true
        } else {
            binding.editPass.isErrorEnabled = false
            false
        }
    }

    private fun signinUser() {
        val res = (verifyEmailId() or verifyPass())
        if (res) return
        val id = "${binding.editID.editText?.text.toString().trim()}"
        val pass = binding.editPass.editText?.text.toString().trim()
        progressDialog.setMessage("Signing In ... ")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(id, pass)
            .addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    val id = "${binding.editID.editText?.text.toString().trim()}"
                    db.collection("User").whereEqualTo("email", id).get()
                        .addOnSuccessListener(OnSuccessListener { queryDocumentSnapshots ->
                            var obj = User()
                            for (doc in queryDocumentSnapshots) {
                                obj = doc.toObject(User::class.java)
                            }

                            db.document("User/${firebaseAuth.currentUser?.email}")
                                .update("fcmToken", SharedPref.getInstance(applicationContext).getToken())
                                .addOnCompleteListener(OnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            this@SignInActivity,
                                            "Registered for Notifications Successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            this@SignInActivity,
                                            "Registration for Notifications Failed!\nPlease Sign in Again to Retry",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })

                            if (obj.type == 0) {
                                progressDialog.cancel()
                                Toast.makeText(this@SignInActivity, "Signed in!", Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(Intent(applicationContext, UserHome::class.java))

                                finish()
                            } else {
                                progressDialog.cancel()
                                Toast.makeText(this@SignInActivity, "Signed in!", Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(Intent(applicationContext, AdminHome::class.java))
                                finish()
                            }
                        })
                } else {
                    progressDialog.cancel()
                    Toast.makeText(this@SignInActivity, "Wrong Credentials or Bad Connection! Try Again", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
