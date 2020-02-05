package com.example.qrreaderscan.bean

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Code() : Serializable {

    var title = ""
    var type = 0
    var txtType = ""
    var date = ""
    var orderDate: Long = 0
    var img = 0
    var flagDone = false
    var tables: ArrayList<String>? = null

    constructor(title: String, type: Int) : this() {
        this.title = title
        this.type = type
    }

    init {
        var date = Date();
        val formatter = SimpleDateFormat("MMM dd")
        val formatterInt = SimpleDateFormat("yyyyMMddHmss")
        this.date = formatter.format(date).capitalize()
        this.orderDate = formatterInt.format(date).toLong()
        this.tables = ArrayList()
        //Log.d("answer", this.date  + " - " + orderDate.toString())
    }

    override fun toString(): String {
        return "Code(title='$title', txtType='$txtType', tables=$tables)"
    }


}