package com.example.librarymanagementsystem



import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagementsystem.databinding.ActivityAdminHomeBinding
import com.google.firebase.auth.FirebaseAuth

class AdminHome : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAdminHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchBook.setOnClickListener(this)
        binding.addBook.setOnClickListener(this)
        binding.removeBook.setOnClickListener(this)
        binding.collect1.setOnClickListener(this)
        binding.updateBook.setOnClickListener(this)
        binding.issueBook.setOnClickListener(this)
        binding.returnBook.setOnClickListener(this)
        binding.logOut.setOnClickListener(this)
        binding.reissueBook.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            binding.logOut -> {
                FirebaseAuth.getInstance().signOut()
                // Your log out logic here
                startActivity(Intent(this, SignInActivity::class.java))
                // Finish the current activity to prevent the user from coming back with the back button
                finish()
            }
            binding.searchBook -> {
                startActivity(Intent(applicationContext, SearchBookSet::class.java))
            }
            binding.addBook -> {
                startActivity(Intent(applicationContext, AdminAddBook::class.java))
            }
            binding.removeBook -> {
                startActivity(Intent(applicationContext, AdminRemoveBook::class.java))
            }
            binding.collect1 -> {
                startActivity(Intent(applicationContext, AdminCollectFine::class.java))
            }
            binding.updateBook -> {
                startActivity(Intent(applicationContext, AdminUpdateBook::class.java))
            }
            binding.issueBook -> {
                startActivity(Intent(applicationContext, AdminIssueBook::class.java))
            }
            binding.returnBook -> {
                startActivity(Intent(applicationContext, AdminReturnBook::class.java))
            }
            binding.reissueBook -> {
                startActivity(Intent(applicationContext, AdminReissueBook::class.java))
            }
        }
    }
}
