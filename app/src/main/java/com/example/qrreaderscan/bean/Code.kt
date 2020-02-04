package com.example.qrreaderscan.bean

import com.example.qrreaderscan.R
import com.google.android.gms.vision.barcode.Barcode

class Code() {

    var title = ""
    var type = 0
    var date = ""
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

    constructor(barcode: Barcode) : this() {
        this.title = barcode.displayValue
        this.type = barcode.valueFormat
        getImgType(barcode.valueFormat)
    }

    private fun getImgType(typeFormat: Int) {
        return when (typeFormat) {
            Barcode.WIFI -> this.img =
                R.drawable.ic_wifi
            Barcode.CALENDAR_EVENT -> this.img =
                R.drawable.ic_event
            Barcode.CONTACT_INFO -> this.img =
                R.drawable.ic_account_circle
            else -> this.img =
                R.drawable.ic_unknown_late
        }
    }

}