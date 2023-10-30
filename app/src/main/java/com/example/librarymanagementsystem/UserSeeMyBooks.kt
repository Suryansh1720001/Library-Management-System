    package com.example.librarymanagementsystem

    import android.app.ProgressDialog
    import android.os.Bundle
    import android.widget.TextView
    import androidx.appcompat.app.AppCompatActivity
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.google.android.gms.tasks.Task
    import com.google.firebase.FirebaseApp
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.firestore.DocumentSnapshot
    import com.google.firebase.firestore.FirebaseFirestore
    import java.util.ArrayList
    import java.util.Calendar

    class UserSeeMyBooks : AppCompatActivity() {
        private lateinit var db: FirebaseFirestore
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var ifNoBook1: TextView
        private val U = User()
        private val B  = Book()


        private lateinit var progressDialog: ProgressDialog

        private val l = ArrayList<Int>()
        private lateinit var myBook: MyBook
        private lateinit var adapter: RecyclerView.Adapter<*>
        private var i = 0
        private var j = 0

        private val myBooks = ArrayList<MyBook>()

        private lateinit var recyclerView: RecyclerView

        override fun onBackPressed() {
            finish()
            super.onBackPressed()
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_user_see_my_books)
            FirebaseApp.initializeApp(this)
            db = FirebaseFirestore.getInstance()
            firebaseAuth = FirebaseAuth.getInstance()
            ifNoBook1 = findViewById(R.id.ifNoBook1)
            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Please Wait ...")
            progressDialog.setCancelable(false)
            recyclerView = findViewById(R.id.recycle1)
            progressDialog.show()
            showBook()

            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = MyBookAdapter(myBooks)
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
        }

        private fun setBook(i: Int) {
            j = 0

            db.document("Book/${U.book[i] / 100}")
                .get()
                .addOnCompleteListener { task: Task<DocumentSnapshot> ->
                    val bookData = task.result?.toObject(Book::class.java)
                    if (bookData != null) {
                        B.title = bookData.title
                        B.type = bookData.type

                        val d = U.date[j].toDate()
                        val c = Calendar.getInstance()
                        c.time = d
                        c.add(Calendar.DAY_OF_MONTH, 14)
                        val reissueDate = c.time

                        myBooks.add(MyBook(U.book[j], B.title, B.type, d, reissueDate))
                        adapter.notifyDataSetChanged()
                        j++
                    }
                }
        }

        private fun showBook() {
            db.document("User/${firebaseAuth.currentUser?.email}")
                .get()
                .addOnCompleteListener { task: Task<DocumentSnapshot> ->
                    if (task.isSuccessful) {
                        val userData = task.result?.toObject(User::class.java)
                        if (userData != null) {
                            U.email = userData.email
                            U.book = userData.book
                            U.date = userData.date

                            if (!U.book.isEmpty()) {
                                l.addAll(U.book)
                                for (i in 0 until l.size) {
                                    setBook(i)
                                }
                                progressDialog.cancel()
                            } else {
                                progressDialog.cancel()
                                ifNoBook1.text = "YOU HAVE NO ISSUED BOOKS!"
                                ifNoBook1.textSize = 18f
                            }
                        }
                    }
                }
        }
    }
