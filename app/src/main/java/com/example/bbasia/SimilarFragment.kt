package com.example.bbasia

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


class SimilarFragment : DialogFragment() {

    private lateinit var companyTv: TextView
    private lateinit var newsTv: TextView
    private lateinit var eqTv: TextView
    private lateinit var bsTv: TextView
    private lateinit var patternTv: TextView

    private lateinit var cvName1: TextView
    private lateinit var cvName2: TextView

    private lateinit var text10: TextView
    private lateinit var text11: TextView
    private lateinit var text14: TextView
    private lateinit var text15: TextView
    private lateinit var text16: TextView
    private lateinit var text17: TextView
    private lateinit var text20: TextView

    private var userId: Int = 2
    private var categoryId: Int = 0

    private lateinit var loadingOverlay: View
    private lateinit var chart: LineChart


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen)

        arguments?.let {
            userId = it.getInt("user_id")
            categoryId = it.getInt("category_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.similar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        companyTv = view.findViewById(R.id.similar_company_tv)
        newsTv = view.findViewById(R.id.similar_news)
        eqTv = view.findViewById(R.id.similar_cv_tv4)
        bsTv = view.findViewById(R.id.similar_cv2_tv4)
        patternTv = view.findViewById(R.id.similar_cv2_tv9)
        loadingOverlay = view.findViewById(R.id.loading_overlay)
        chart = view.findViewById(R.id.similar_cv2_iv1)

        cvName1 = view.findViewById(R.id.similar_cv2_tv1)
        cvName2 = view.findViewById(R.id.similar_cv2_tv2)

        text10 = view.findViewById(R.id.similar_cv2_tv10)
        text11 = view.findViewById(R.id.similar_cv2_tv11)
        text14 = view.findViewById(R.id.similar_cv2_tv14)
        text15 = view.findViewById(R.id.similar_cv2_tv15)
        text16 = view.findViewById(R.id.similar_cv2_tv16)
        text17 = view.findViewById(R.id.similar_cv2_tv17)
        text20 = view.findViewById(R.id.similar_cv2_tv20)


        view.findViewById<ImageButton>(R.id.similar_cancel_btn)?.setOnClickListener {
            dismiss()
        }

        showLoading(true)
        fetchTopPickData(userId, categoryId)
    }

    private fun showLoading(show: Boolean) {
        loadingOverlay.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun renderChart(chartData: List<ChartStockData>) {
        val lineDataSets = mutableListOf<ILineDataSet>()
        val xLabels = chartData.firstOrNull()?.data?.map { it.date } ?: emptyList()

        val colors = listOf(
            Color.parseColor("#F0B26B"),
            Color.parseColor("#8DC8E8"),
            Color.parseColor("#FFB0CF"),
            Color.parseColor("#69B319"),
            Color.parseColor("#FFB4EB"),
            Color.parseColor("#9D71BD"),
//            Color.parseColor("#A9A9A9"),
//            Color.parseColor("#000000"),
//            Color.parseColor("#FFFF00"),
//            Color.parseColor("#FFA500"),
        )

        chartData.forEachIndexed { index, stock ->
            val entries = stock.data.mapIndexed { i, point ->
                Entry(i.toFloat(), point.closing_price.toFloat())
            }

            val trimmedLabel = if (stock.stock_name.length > 3)
                stock.stock_name.substring(0, 3) + "..."
            else stock.stock_name

            val dataSet = LineDataSet(entries, trimmedLabel).apply {
                color = colors[index % colors.size]
                setCircleColor(color)
                lineWidth = 2f
                setDrawCircles(false)
                setDrawValues(false)
            }

            lineDataSets.add(dataSet)
        }

        chart.apply {
            data = LineData(lineDataSets)
            description.isEnabled = false
            axisRight.isEnabled = false

            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(xLabels)
                position = XAxis.XAxisPosition.BOTTOM
                labelRotationAngle = -45f
                granularity = 1f
                labelCount = (xLabels.size / 5).coerceAtLeast(4) // 날짜 간격 조절
                setDrawGridLines(false)
                setAvoidFirstLastClipping(true)
                textSize = 10f
            }

            legend.apply {
                isEnabled = true
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
                textSize = 12f
            }

            invalidate()
        }
    }



    private fun fetchTopPickData(userId: Int, categoryId: Int) {
        RetrofitClient.instance.getTopPick(userId, categoryId).enqueue(object : Callback<TopPicResponse> {
            override fun onResponse(call: Call<TopPicResponse>, response: Response<TopPicResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        companyTv.text = data.stock_name
                        newsTv.text = "\"${data.news}\""
                        eqTv.text = data.eq.toString()
                        bsTv.text = data.bs.toString()
                        patternTv.text = data.pattern_similarity.toString()

                        cvName1.text = data.stock_name
                        cvName2.text = data.stock_name

                        text10.text = "${data.stock_name}은"
                        text11.text = data.stock_type
                        text14.text = "${data.same_type_cnt}종목"
                        text15.text = "이 ${data.stock_type}에 해당합니다."
                        text16.text = "${data.stock_name}의 주 업종은 "
                        text17.text = data.stock_industry
                        text20.text = "${data.same_industry_cnt}종목"

                        val similarStocksTv = view?.findViewById<TextView>(R.id.similar_cv2_tv7)
                        val stockNames = data.stocks.joinToString(", ") { it.stock_name }
                        similarStocksTv?.text = stockNames

                        renderChart(data.chartData)
                    }
                } else {
                    Log.e("similar", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<TopPicResponse>, t: Throwable) {
                showLoading(false)
                Log.e("similar", "그냥 통신이 안됨")
            }

        })
    }

    companion object {
        fun newInstance(userId: Int, categoryId: Int): SimilarFragment {
            return SimilarFragment().apply {
                arguments = Bundle().apply {
                    putInt("user_id", userId)
                    putInt("category_id", categoryId)
                }
            }
        }
    }
}