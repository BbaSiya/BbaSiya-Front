package com.example.bbasia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CategoryAdapter(
    context: Context,
    private val items: List<CategoryItem>
) : ArrayAdapter<CategoryItem> (context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_category, parent, false)


        val nameTextView1 = view.findViewById<TextView>(R.id.item_category_tv1)
        val nameTextView2 = view.findViewById<TextView>(R.id.item_category_tv2)
        val imageView = view.findViewById<ImageView>(R.id.item_category_iv)

        val item = items[position]
        nameTextView1.text = item.name1
        nameTextView2.text = item.name2
        imageView.setImageResource(item.imageResId)

        return view
    }
}