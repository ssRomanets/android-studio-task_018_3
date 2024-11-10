package com.example.task_018

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ListAdapter(context: Context, personList: MutableList<Product>) :
    ArrayAdapter<Product>(context, R.layout.list_item, personList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val product = getItem(position)
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val imageViewTV = view?.findViewById<ImageView>(R.id.imageViewTV)
        val productNameTV = view?.findViewById<TextView>(R.id.productNameTV)
        val productPriceTV = view?.findViewById<TextView>(R.id.productPriceTV)

        imageViewTV?.setImageURI(Uri.parse(product?.image))
        productNameTV?.text = product?.name
        productPriceTV?.text = product?.price

        return  view!!
    }
}