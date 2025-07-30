package com.example.bbasia

import android.content.Context
import android.util.Log
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
import retrofit2.Call

class ChartAdapter(
    context: Context,
    private val items: List<ChartItem>,
    private val userId: Int,
    private val apiService: ApiService
) : ArrayAdapter<ChartItem>(context, 0, items) {

    private var selectedPosition: Int? = null
    private val newsCache = mutableMapOf<String, StockNewsResponse>()

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

            // CoroutineScope, suspend 함수 제거
            apiService.getStockNews(item.id, userId).enqueue(object : retrofit2.Callback<StockNewsResponse> {
                override fun onResponse(call: Call<StockNewsResponse>, response: retrofit2.Response<StockNewsResponse>) {
                    if (response.isSuccessful) {
                        val newsData = response.body()
                        if (newsData != null) {
                            chartNewsTv.text = newsData.news
                            chartNewsBs.text = "${newsData.bs}%"
                            chartNewsGood.text = newsData.eq.toString()
                            chartNewsGoodIcon.text = when {
                                newsData.eq > 60 -> "☺️"
                                newsData.eq > 30 -> "😐"
                                newsData.eq > 0 -> "😢"
                                else -> " "
                            }
                            newsCache[item.id] = newsData
                        } else {
                            chartNewsTv.text = "뉴스 없음"
                            chartNewsBs.text = ""
                            chartNewsGood.text = ""
                            chartNewsGoodIcon.text = ""
                        }
                    } else {
                        chartNewsTv.text = "뉴스 없음"
                        chartNewsBs.text = ""
                        chartNewsGood.text = ""
                        chartNewsGoodIcon.text = ""
                    }
                }

                override fun onFailure(call: Call<StockNewsResponse>, t: Throwable) {
                    Log.e("News", "Error fetching news", t)
                    chartNewsTv.text = "불러오기 실패"
                    chartNewsBs.text = ""
                    chartNewsGood.text = ""
                    chartNewsGoodIcon.text = ""
                }
            })
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

            notifyDataSetChanged()
        }

        return view
    }
}
