package com.example.qrreaderscan.bean

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


class Code() : Serializable {

    var title = ""
    var type = 0
    var txtType = ""
    var date = ""
    var orderDate: Long = 0
    var img = 0
    var flagDone = false

    var table1: String = ""
    var table2: String = ""
    var table3: String = ""
    var table4: String = ""
    var table5: String = ""
    var table6: String = ""
    var table7: String = ""
    var table8: String = ""
    var table9: String = ""
    var table10: String = ""
    var table11: String = ""
    var table12: String = ""
    var table13: String = ""
    var table14: String = ""
    var table15: String = ""
    var table16: String = ""
    var table17: String = ""
    var table18: String = ""
    var table19: String = ""
    var table20: String = ""

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

        //Log.d("answer", this.date  + " - " + orderDate.toString())
    }

    override fun toString(): String {
        return "Code(title='$title', orderDate=$orderDate)"
    }


}