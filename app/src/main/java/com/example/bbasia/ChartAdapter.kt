package com.example.bbasia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.Visibility

class ChartAdapter (
    context: Context,
    private val items: List<ChartItem>
    ) : ArrayAdapter<ChartItem>(context, 0, items) {

    private var selectedPosition: Int? = null

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.item_chart, parent, false)

            val chartNum = view.findViewById<TextView>(R.id.chart_num)
            val chartName = view.findViewById<TextView>(R.id.chart_name)
            val chartPrice = view.findViewById<TextView>(R.id.chart_now_price)
            val chartMoney = view.findViewById<TextView>(R.id.chart_average_return)
            val chartBuy = view.findViewById<TextView>(R.id.chart_volume_power)
            val chartNews = view.findViewById<TextView>(R.id.chart_name_news)
            val chartCl = view.findViewById<View>(R.id.chart_news)

            val item = items[position]
            chartNum.text = item.rank.toString()
            chartName.text = item.name
            chartPrice.text = item.price.toString()
            chartMoney.text = "${item.money}%"
            chartBuy.text = item.buy.toString()

            if (position == selectedPosition) {
                if (chartCl.visibility != View.VISIBLE) {
                    val slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)
                    chartCl.startAnimation(slideDown)
                    chartCl.visibility = View.VISIBLE
                }
            } else {
                if (chartCl.visibility == View.VISIBLE) {
                    val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
                    chartCl.startAnimation(slideUp)
                    chartCl.visibility = View.GONE
                }
            }

            chartNews.setOnClickListener {
                val prevSelected = selectedPosition
                selectedPosition = if (selectedPosition == position) null else position
                if (prevSelected != null && prevSelected != position) {
                    notifyDataSetChanged()  // 이전 선택 항목 애니메이션 적용
                }
                notifyDataSetChanged()  // 현재 클릭 항목 처리
            }

            return view
        }
}