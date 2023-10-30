package com.example.librarymanagementsystem

class Admin {
    var type: Int = 0
    var name: String = ""
    var email: String = ""
    var fcmToken: String = ""

    constructor() {}

    constructor(type: Int, name: String, email: String) {
        this.type = type
        this.name = name
        this.email = email
    }
}
