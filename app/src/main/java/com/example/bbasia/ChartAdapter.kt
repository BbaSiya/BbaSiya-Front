package com.example.bbasia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChartAdapter(
    context: Context,
    private val items: List<ChartItem>,
    private val userId: Int,
    private val apiService: ApiService
) : ArrayAdapter<ChartItem>(context, 0, items) {

    private var selectedPosition: Int? = null
    private val newsCache = mutableMapOf<Int, StockNewsResponse>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_chart, parent, false)

        val chartNum = view.findViewById<TextView>(R.id.chart_num)
        val chartName = view.findViewById<TextView>(R.id.chart_name)
        val chartPrice = view.findViewById<TextView>(R.id.chart_now_price)
        val chartChange = view.findViewById<TextView>(R.id.chart_average_change)
        val chartBuy = view.findViewById<TextView>(R.id.chart_volume_power)
        val chartNews = view.findViewById<TextView>(R.id.chart_name_news)
        val chartCl = view.findViewById<View>(R.id.chart_news)

        val chartNewsTv = view.findViewById<TextView>(R.id.chart_news_tv)
        val chartNewsBs = view.findViewById<TextView>(R.id.chart_news_bs2)
        val chartNewsGood = view.findViewById<TextView>(R.id.chart_news_good2)
        val chartNewsGoodIcon = view.findViewById<TextView>(R.id.chart_news_good1)

        val item = items[position]
        chartNum.text = "${position + 1}"
        chartName.text = item.name
        val priceFormatted = String.format("%,d", item.current_price)
        chartPrice.text = priceFormatted
        chartChange.text = "${item.updown_rate}%"
        chartBuy.text = item.volume_power.toString()

        if (position == selectedPosition) {
            if (chartCl.visibility != View.VISIBLE) {
                val slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)
                chartCl.startAnimation(slideDown)
                chartCl.visibility = View.VISIBLE
            }

            val cached = newsCache[item.id.toInt()]
            if (cached != null) {
                chartNewsTv.text = cached.news
                chartNewsBs.text = "${cached.bs}%,"
                chartNewsGood.text = cached.eq.toString()
                if (cached.bs > 60) {
                    chartNewsGoodIcon.text = "☺️"
                } else if (cached.bs > 30) {
                    chartNewsGoodIcon.text = "😐"
                } else {
                    chartNewsGoodIcon.text = "😢"
                }
            } else {
                chartNewsTv.text = "뉴스 로딩 중..."
                chartNewsBs.text = ""
                chartNewsGood.text = ""

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = apiService.getStockNews(item.id.toInt(), userId)
                        val newsData = response.firstOrNull()
                        withContext(Dispatchers.Main) {
                            if (newsData != null && position == selectedPosition) {
                                chartNewsTv.text = newsData.news
                                chartNewsBs.text = newsData.bs.toString()
                                chartNewsGood.text = newsData.eq.toString()
                                newsCache[item.id.toInt()] = newsData
                            } else {
                                chartNewsTv.text = "뉴스 없음"
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            chartNewsTv.text = "불러오기 실패"
                        }
                    }
                }
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
                notifyDataSetChanged()
            }
            notifyDataSetChanged()
        }

        return view
    }
}
