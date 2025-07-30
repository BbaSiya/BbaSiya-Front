package com.example.bbasia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView

class CategoryAdapter(
    context: Context,
    private val items: List<CategoryItem>
) : ArrayAdapter<CategoryItem> (context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_category, parent, false)

        val cv = view.findViewById<CardView>(R.id.item_category_cv)
        if (position == 0) {
            cv.visibility = View.VISIBLE
        } else {
            cv.visibility = View.GONE
        }

        val item = items[position]

        val nameTextView1 = view.findViewById<TextView>(R.id.item_category_tv1)
        val nameTextView2 = view.findViewById<TextView>(R.id.item_category_tv2)

        val nameParts = item.name.split("\\n", limit = 2)
        nameTextView1.text = nameParts.getOrNull(0) ?: ""
        nameTextView2.text = nameParts.getOrNull(1) ?: ""


        val imageView = view.findViewById<ImageView>(R.id.item_category_iv)
        val imageResId = context.resources.getIdentifier("img_${position + 1}", "drawable", context.packageName)
        if (imageResId != 0) {
            imageView.setImageResource(imageResId)
        }

        return view
    }
}