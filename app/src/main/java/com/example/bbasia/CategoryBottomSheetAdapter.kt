package com.example.bbasia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView

class CategoryBottomSheetAdapter (
    context: Context,
    private val items: List<CategoryBottomSheetItem>
) : ArrayAdapter<CategoryBottomSheetItem>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_category_name, parent, false)


        val nameTv = view.findViewById<TextView>(R.id.item_category_name_tv)
        val newCard = view.findViewById<CardView>(R.id.item_category_name_cv)
        val nowIv = view.findViewById<ImageView>(R.id.item_category_name_now)

        val item = items[position]
        nameTv.text = item.name
        if (item.new) {
            newCard.visibility = View.VISIBLE
        } else {
            newCard.visibility = View.GONE
        }

        if (item.now) {
            nowIv.visibility = View.VISIBLE
        } else {
            nowIv.visibility = View.GONE
        }

        return view
    }
}