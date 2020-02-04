package com.example.qrreaderscan.factory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qrreaderscan.R
import com.example.qrreaderscan.bean.Code
import com.example.qrreaderscan.listener.ClickListener

class AdapterCustom(items: List<Code>, var listener: ClickListener) :
    RecyclerView.Adapter<AdapterCustom.ViewHolder>() {

    var items: List<Code>? = null

    init {
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCustom.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.template_barcode, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }

    override fun onBindViewHolder(holder: AdapterCustom.ViewHolder, position: Int) {
        val item = items?.get(position)
        holder.imageView?.setImageResource(item?.img!!)
        holder.date?.text = item?.date
        holder.title?.text = item?.title

    }


    class ViewHolder(itemView: View, listener: ClickListener) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var view = itemView
        var title: TextView? = null
        var date: TextView? = null
        var imageView: ImageView? = null
        var listener: ClickListener? = null

        init {
            this.title = view.findViewById(R.id.tvt_title)
            this.date = view.findViewById(R.id.tvt_date)
            this.imageView = view.findViewById(R.id.imageViewIcon)
            this.listener = listener
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            this.listener?.onClick(itemView!!, adapterPosition)
        }
    }
}