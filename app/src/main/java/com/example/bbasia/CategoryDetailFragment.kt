package com.example.bbasia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.Fragment

class CategoryDetailFragment : Fragment() {

    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.category_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = view.findViewById(R.id.chart_lv)

        val chartItems = listOf(
            ChartItem(1, "HD현대 중공업", 372000, 60564, 512, 10),
            ChartItem(1, "HD현대 중공업", 372000, 60564, 512, 10),
            ChartItem(1, "HD현대 중공업", 372000, 60564, 512, 10),
            ChartItem(1, "HD현대 중공업", 372000, 60564, 512, 10),
            ChartItem(1, "HD현대 중공업", 372000, 60564, 512, 10),
            ChartItem(1, "HD현대 중공업", 372000, 60564, 512, 10),
            ChartItem(1, "HD현대 중공업", 372000, 60564, 512, 10),
            ChartItem(1, "HD현대 중공업", 372000, 60564, 512, 10),
            ChartItem(1, "HD현대 중공업", 372000, 60564, 512, 10),
            ChartItem(1, "HD현대 중공업", 372000, 60564, 512, 10),
            ChartItem(1, "HD현대 중공업", 372000, 60564, 512, 10),
            ChartItem(1, "HD현대 중공업", 372000, 60564, 512, 10)
            )

        val adapter = ChartAdapter(requireContext(), chartItems)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = chartItems[position]

        }

        val similarBtn = view.findViewById<ImageButton>(R.id.main_similar_btn)
        similarBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fl, SimilarFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}