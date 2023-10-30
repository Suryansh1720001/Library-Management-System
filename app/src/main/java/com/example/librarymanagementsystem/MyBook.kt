package com.example.librarymanagementsystem

import java.util.Date

data class MyBook(
    var Bid: Int,
    var Title: String,
    var Type: String,
    var Idate: Date,
    var Ddate: Date
) {
    constructor() : this(0, "", "", Date(), Date())
}
