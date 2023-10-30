package com.example.librarymanagementsystem


class Book {
    var title: String = ""
    var type: String = ""
    var total: Int = 0
    var available: Int = 0
    var id: Int = 0
    var unit: MutableList<Int> = mutableListOf()

    constructor()

    constructor(title: String, type: String, total: Int, id: Int) {
        this.title = title
        this.type = type
        this.total = total
        this.id = id
        this.available = total
    }
}


//data class Book(
//    val id: String,       // Change the data type as needed (e.g., String or Int)
//    var type: String,
//    val available: Int,   // Change the data type as needed
//    var title: String,
//    val total: Int
//)

