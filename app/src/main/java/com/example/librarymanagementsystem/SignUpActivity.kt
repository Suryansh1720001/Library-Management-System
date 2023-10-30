package com.example.librarymanagementsystem
import android.app.ProgressDialog
import android.content.Intent
import com.google.firebase.firestore.FirebaseFirestore


import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagementsystem.databinding.ActivitySignupBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var type: String = ""
    private var type1: Int = 0
    private var temp: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.buttonRegister.setOnClickListener(this)
        binding.toSignIn.setOnClickListener(this)
        binding.check1.setOnClickListener(this)

        val list = listOf("Select Account Type", "User", "Admin")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.userType.adapter = adapter
        binding.userType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                type = selectedItem

                when (selectedItem) {
                    "Select Account Type" -> disableFields()
                    "User" -> enableUserFields()
                    "Admin" -> enableAdminFields()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        FirebaseApp.initializeApp(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.check1 -> {
                binding.buttonRegister.isEnabled = binding.check1.isChecked
            }
            binding.buttonRegister -> registerUser()
            binding.toSignIn -> {
                startActivity(Intent(applicationContext, SignInActivity::class.java))
                finish()
            }
        }
    }

    private fun enableUserFields() {
        binding.editPass1.isEnabled = true
        binding.editPass.isEnabled = true
        binding.editName.isEnabled = true
        binding.editID.isEnabled = true
        binding.editEnrollNo.isEnabled = true
        binding.editCardNo.isEnabled = true
        clearErrors()
    }

    private fun enableAdminFields() {
        binding.editPass1.isEnabled = true
        binding.editPass.isEnabled = true
        binding.editName.isEnabled = true
        binding.editID.isEnabled = true
        binding.editEnrollNo.isEnabled = false
        binding.editCardNo.isEnabled = false
        clearErrors()
    }

    private fun disableFields() {
        binding.editPass1.isEnabled = false
        binding.editPass.isEnabled = false
        binding.editName.isEnabled = false
        binding.editID.isEnabled = false
        binding.editEnrollNo.isEnabled = false
        binding.editCardNo.isEnabled = false
        clearErrors()
    }

    private fun verifyName(): Boolean {
        val name = binding.editName.editText?.text.toString().trim()
        return if (name.isEmpty()) {
            binding.editName.error = "Name Required"
            true
        } else {
            binding.editName.error = null
            false
        }
    }

    private fun verifyCardNo(): Boolean {
        val cardNo = binding.editCardNo.editText?.text.toString().trim()
        return if (cardNo.isEmpty()) {
            binding.editCardNo.error = "Card No. Required"
            true
        } else {
            binding.editCardNo.error = null
            false
        }
    }

    private fun verifyEnrollNo(): Boolean {
        val enrollNo = binding.editEnrollNo.editText?.text.toString().trim()
        return if (enrollNo.isEmpty()) {
            binding.editEnrollNo.error = "Enrollment No. Required"
            true
        } else {
            binding.editEnrollNo.error = null
            false
        }
    }

    private fun verifyEmailId(): Boolean {
        val emailId = binding.editID.editText?.text.toString().trim()
        return when {
            emailId.isEmpty() -> {
                binding.editID.error = "Email ID Required"
                true
            }
            !emailId.endsWith("@gmail.com") -> {
                binding.editID.error = "Enter Valid Email ID"
                true
            }
            else -> {
                binding.editID.error = null
                false
            }
        }
    }

    private fun verifyPass(): Boolean {
        val pass = binding.editPass.editText?.text.toString().trim()
        return if (pass.isEmpty()) {
            binding.editPass.error = "Password Required"
            true
        } else {
            binding.editPass.error = null
            false
        }
    }

    private fun verifyPass1(): Boolean {
        val pass1 = binding.editPass1.editText?.text.toString().trim()
        val pass = binding.editPass.editText?.text.toString().trim()
        return when {
            pass1.isEmpty() -> {
                binding.editPass1.error = "Confirm Password Required"
                true
            }
            pass != pass1 -> {
                binding.editPass1.error = "Passwords do not match"
                true
            }
            else -> {
                binding.editPass1.error = null
                false
            }
        }
    }

    private fun verifyType(): Boolean {
        return if (type == "Select Account Type") {
            Toast.makeText(this, "Please select account type!", Toast.LENGTH_SHORT).show()
            true
        } else {
            false
        }
    }

    private fun verifyCard1(): Boolean {
        db.collection("User")
            .whereEqualTo("card", binding.editCardNo.editText?.text.toString().toInt())
            .get()
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    temp = task.result?.size() ?: 0
                }
            })

        return temp != 0
    }

    private fun clearErrors() {
        binding.editName.error = null
        binding.editCardNo.error = null
        binding.editEnrollNo.error = null
        binding.editID.error = null
        binding.editPass.error = null
        binding.editPass1.error = null
    }

    private fun registerUser() {
        if (verifyType()) return

        val res = when (type) {
            "User" -> verifyName() || verifyCardNo() || verifyEmailId() || verifyEnrollNo() || verifyPass() || verifyPass1()
            "Admin" -> verifyName() || verifyEmailId() || verifyPass() || verifyPass1()
            else -> false
        }

        if (res) return

        val id = binding.editID.editText?.text.toString().trim()
        val pass = binding.editPass

            .editText?.text.toString().trim()

        if (type == "User") {
            type1 = 0
        } else {
            type1 = 1
        }

        progressDialog.setMessage("Registering User ...")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(id, pass)
            .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    val id = binding.editID.editText?.text.toString().trim()
                    val name = binding.editName.editText?.text.toString().trim()

                    if (type1 == 0) {
                        val enroll = binding.editEnrollNo.editText?.text.toString().trim().toInt()
                        val card = binding.editCardNo.editText?.text.toString().trim().toInt()

                        db.collection("User")
                            .document(id)
                            .set(User(name, id, enroll, card, type1))
                            .addOnSuccessListener(OnSuccessListener {
                                progressDialog.cancel()
                                Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show()
                                firebaseAuth.signOut()
                                startActivity(Intent(applicationContext, SignInActivity::class.java))
                                finish()
                            })
                            .addOnFailureListener(OnFailureListener {
                                Toast.makeText(this, "Please Try Again!", Toast.LENGTH_SHORT).show()
                            })
                    } else {
                        db.collection("User")
                            .document(id)
                            .set(Admin(type1, name, id))
                            .addOnSuccessListener(OnSuccessListener {
                                progressDialog.cancel()
                                Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show()
                                firebaseAuth.signOut()
                                startActivity(Intent(applicationContext, SignInActivity::class.java))
                                finish()
                            })
                            .addOnFailureListener(OnFailureListener {
                                Toast.makeText(this, "Please Try Again!", Toast.LENGTH_SHORT).show()
                            })
                    }
                } else {
                    progressDialog.cancel()
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        Toast.makeText(this, "Already Registered!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Registration Failed! Try Again", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }
}
