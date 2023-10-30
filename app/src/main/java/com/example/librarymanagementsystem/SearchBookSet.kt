package com.example.librarymanagementsystem


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagementsystem.databinding.ActivitySearchBookSetBinding
import com.google.android.material.textfield.TextInputLayout

import com.google.firebase.FirebaseApp

class SearchBookSet : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBookSetBinding
    private lateinit var editTitle3: TextInputLayout
    private lateinit var editBid3: TextInputLayout
    private lateinit var spinner3: Spinner
    private lateinit var button3: Button
    private lateinit var checkBox: CheckBox
    private var type: String = ""

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun verifyTitle(): Boolean {
        val t = editTitle3.editText?.text.toString().trim()
        return !t.isEmpty()
    }

    private fun verifyBid(): Boolean {
        val b = editBid3.editText?.text.toString().trim()
        return !b.isEmpty()
    }

    private fun verifyCategory(): Boolean {
        return type != "Select Book Category"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBookSetBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        FirebaseApp.initializeApp(this)
        editTitle3 = binding.editTitle3
        editBid3 = binding.editBid3
        spinner3 = binding.spinner3
        button3 = binding.button3
        checkBox = binding.onlyAvailable

        val A = resources.getStringArray(R.array.list1)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, A)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner3.adapter = adapter
        spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        button3.setOnClickListener {
            if (!(verifyCategory() || verifyTitle() || verifyBid())) {
                Toast.makeText(this@SearchBookSet, "Select at least one parameter!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(applicationContext, SearchBook::class.java)

            if (verifyBid() && checkBox.isChecked) {
                intent.putExtra("id", 1)
                intent.putExtra("bid", editBid3.editText?.text.toString().trim().toInt())
                startActivity(intent)
            } else if (verifyBid() && !checkBox.isChecked) {
                intent.putExtra("id", 2)
                intent.putExtra("bid", editBid3.editText?.text.toString().trim().toInt())
                startActivity(intent)
            } else if (verifyTitle() && verifyCategory() && checkBox.isChecked) {
                intent.putExtra("id", 3)
                intent.putExtra("btitle", editTitle3.editText?.text.toString().trim())
                intent.putExtra("btype", type)
                startActivity(intent)
            } else if (verifyTitle() && verifyCategory() && !checkBox.isChecked) {
                intent.putExtra("id", 4)
                intent.putExtra("btitle", editTitle3.editText?.text.toString().trim())
                intent.putExtra("btype", type)
                startActivity(intent)
            } else if (verifyTitle() && !verifyCategory() && checkBox.isChecked) {
                intent.putExtra("id", 5)
                intent.putExtra("btitle", editTitle3.editText?.text.toString().trim())
                startActivity(intent)
            } else if (verifyTitle() && !verifyCategory() && !checkBox.isChecked) {
                intent.putExtra("id", 6)
                intent.putExtra("btitle", editTitle3.editText?.text.toString().trim())
                startActivity(intent)
            } else if (!verifyTitle() && verifyCategory() && checkBox.isChecked) {
                intent.putExtra("id", 7)
                intent.putExtra("btype", type)
                startActivity(intent)
            } else if (!verifyTitle() && verifyCategory() && !checkBox.isChecked) {
                intent.putExtra("id", 8)
                intent.putExtra("btype", type)
                startActivity(intent)
            }
        }
    }
}
