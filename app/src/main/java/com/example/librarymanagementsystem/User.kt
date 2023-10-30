package com.example.librarymanagementsystem
//
//import androidx.databinding.BaseObservable
//import androidx.databinding.Bindable
//import androidx.databinding.ObservableArrayList
//import androidx.databinding.ObservableField
//import com.google.firebase.Timestamp
//
//class User : BaseObservable() {
//    var name: String? = null
//    var email: String? = null
//    val book: ObservableArrayList<Int> = ObservableArrayList()
//    val fine: ObservableArrayList<Int> = ObservableArrayList()
//    val re: ObservableArrayList<Int> = ObservableArrayList()
//    val date: ObservableArrayList<Timestamp> = ObservableArrayList()
//    var enroll: Int = 0
//    var card: Int = 0
//    var type: Int = 0
//    var leftFine: Int = 0
//
//    @get:Bindable
//    var fcmToken: String? = null
//        set(value) {
//            field = value
//            notifyPropertyChanged(BR.fcmToken)
//        }
//
//   @Bindable
//    fun getLeftFine(): Int {
//        return leftFine
//    }
//
//    fun setLeftFine(leftFine: Int) {
//        this.leftFine = leftFine
//        notifyPropertyChanged(BR.leftFine)
//    }
//
//    constructor(name: String?, email: String?, enroll: Int, card: Int, type: Int) {
//        this.name = name
//        this.email = email
//        this.enroll = enroll
//        this.card = card
//        this.type = type
//    }
//}

//import com.google.firebase.Timestamp
//
//
//class User {
//    var name: String = ""
//    var email: String = ""
//    var book: List<Int> = ArrayList()
//    var fine: List<Int> = ArrayList()
//    var re: List<Int> = ArrayList()
//    var date: List<Timestamp> = ArrayList()
//    var enroll: Int = 0
//    var card: Int = 0
//    var type: Int = 0
//    var fcmToken: String =""
//    var leftFine: Int = 0
//
//    constructor() {
//        // Default constructor
//    }
//
//    constructor(name: String, email: String, enroll: Int, card: Int, type: Int) {
//        this.name = name
//        this.email = email
//        this.enroll = enroll
//        this.card = card
//        this.type = type
//    }
//
//    fun getFcmToken(): String {
//        return fcmToken
//    }
//
//    fun setFcmToken(fcmToken: String) {
//        this.fcmToken = fcmToken
//    }
//
//    fun getLeftFine(): Int {
//        return leftFine
//    }
//
//    fun setLeftFine(leftFine: Int) {
//        this.leftFine = leftFine
//    }
//
//
//    fun fetchFcmToken(fcmToken: String){
//        this.fcmToken = fcmToken
//    }
//
//
//}



import com.google.firebase.Timestamp

class User {
    var name: String? = null
    var email: String? = null
    var book: List<Int> = ArrayList()
    var fine: List<Int> = ArrayList()
    var re: List<Int> = ArrayList()
    var date: List<Timestamp> = ArrayList()
    var enroll = 0
    var card = 0
    var type = 0
    var fcmToken: String? = null
    var leftFine = 0

    constructor() {}

    constructor(name: String?, email: String?, enroll: Int, card: Int, type: Int) {
        this.name = name
        this.email = email
        this.enroll = enroll
        this.card = card
        this.type = type
    }
}
