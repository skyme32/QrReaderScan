package com.example.qrreaderscan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qrreaderscan.bean.Code
import com.example.qrreaderscan.factory.AdapterCustom
import com.example.qrreaderscan.listener.ClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var adapterCustom: AdapterCustom? = null
    var listRecycle: RecyclerView? = null


    companion object {
        var codes = ArrayList<Code>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //Only for testing
        texting()

        //
        listRecycle = findViewById(R.id.qrRecyclerList)
        listRecycle?.setHasFixedSize(true)
        listRecycle?.layoutManager = LinearLayoutManager(this)


        // Implement onClick and OnLongClick to the Elements of the list
        var click = createAdapterOnclick()
        adapterCustom = AdapterCustom(sortArraylist(codes), click)
        listRecycle?.adapter = adapterCustom


        /**
        // Simulated the infoarmation
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swiprefresh)
        swipeRefreshLayout.setOnRefreshListener {

        var code1 = Code().apply {
        img = R.drawable.ic_wifi
        title = "Asto_es_bueno"
        date = "28/01/2020"
        type = 9
        }
        codes.add(code1)
        adapterCustom = AdapterCustom(sortArraylist(codes), click)
        listRecycle?.adapter = adapterCustom


        swipeRefreshLayout.isRefreshing = false
        }
         **/



        fab.setOnClickListener { view ->
            val intent = Intent(view.context, CameraActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sortArraylist(list: ArrayList<Code>): List<Code> {
        return list.sortedByDescending { it.orderDate }

        //return list.sortedWith(compareBy { it.orderDate })
    }

    private fun showSortDialog() {
        //var options = {"Ascending", "Descending"}

    }

    private fun createAdapterOnclick(): ClickListener {
        // Implement onclick to the Elements of the list, only one click
        return object : ClickListener {
            override fun onClick(itemView: View, index: Int) {
                Toast.makeText(
                    applicationContext,
                    index.toString() + " - " + codes[index].title,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun texting() {
        var code2 = Code().apply {
            img = R.drawable.ic_event
            title = "Es tu dia"
            orderDate = 20190103120112
            txtType = getString(R.string.type_event)
            type = 11
        }

        var code1 = Code().apply {
            img = R.drawable.ic_wifi
            title = "Esto_es_bueno"
            orderDate = 20190102120112
            txtType = getString(R.string.type_wifi)
            type = 9
        }

        var code3 = Code().apply {
            img = R.drawable.ic_account_circle
            title = "Skyme32"
            orderDate = 20190104120112
            txtType = getString(R.string.type_contact)
            type = 1
        }

        var code4 = Code().apply {
            img = R.drawable.ic_unknown_late
            title = "Texto normal y corriente"
            orderDate = 20190105120112
            txtType = getString(R.string.type_unknown)
            type = 14
        }


        codes.add(code1)
        codes.add(code2)
        codes.add(code3)
        codes.add(code4)

        Log.d("answer", codes.toString())
    }
}
