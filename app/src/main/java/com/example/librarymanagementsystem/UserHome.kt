package com.example.librarymanagementsystem

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagementsystem.databinding.ActivityUserHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class UserHome : AppCompatActivity() {
    private lateinit var binding: ActivityUserHomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.searchBook1.setOnClickListener {
            startActivity(Intent(applicationContext, SearchBookSet::class.java))
        }

        binding.seeBook.setOnClickListener {
            startActivity(Intent(applicationContext, UserSeeMyBooks::class.java))
        }

        binding.logOut1.setOnClickListener {
//            db.document("User/${firebaseAuth.currentUser?.email}")
//                .update("fcmToken", null)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        FirebaseAuth.getInstance().signOut()
//                        startActivity(Intent(applicationContext, SignInActivity::class.java))
//                        finish()
//                    } else {
//                        Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show()
//                    }
//                }

            FirebaseAuth.getInstance().signOut()
            // Your log out logic here
            startActivity(Intent(this, SignInActivity::class.java))
            // Finish the current activity to prevent the user from coming back with the back button
            finish()
        }

        binding.buttonReissue.setOnClickListener {
            startActivity(Intent(applicationContext, UserReissueBook::class.java))
        }
    }
}
