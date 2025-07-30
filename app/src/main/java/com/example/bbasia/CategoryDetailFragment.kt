package com.example.bbasia

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryDetailFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var apiService: ApiService
    private var userId: Int = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.category_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectBoxTv = view.findViewById<TextView>(R.id.main_selectbox_tv)
        val detailTv1 = view.findViewById<TextView>(R.id.main_text1)
        val detailTv2 = view.findViewById<TextView>(R.id.main_text2)
        val detailTv3 = view.findViewById<TextView>(R.id.main_text3)

        val categoryName = arguments?.getString("category_name")?.replace("\\n", " ")
        categoryName?.let {
            selectBoxTv.text = it
        }

        val description = arguments?.getString("category_description")
        description?.let {
            val nameParts = description.split("\\n", limit = 3)
            detailTv1.text = nameParts.getOrNull(0) ?: ""
            detailTv2.text = nameParts.getOrNull(1) ?: ""
            detailTv3.text = nameParts.getOrNull(2) ?: ""
        }

        listView = view.findViewById(R.id.chart_lv)
        apiService = RetrofitClient.instance

        val categoryId = arguments?.getInt("category_id") ?: return

        callCategoryApi(categoryId)


        this.userId = 2 // 실제 유저 ID로 수정 필요

        val similarBtn = view.findViewById<ImageButton>(R.id.main_similar_btn)
        similarBtn.setOnClickListener {
            val dialog = SimilarFragment.newInstance(userId, categoryId)
            dialog.show(parentFragmentManager, "SimilarFragment")
        }

        val selectBtn = view.findViewById<View>(R.id.main_selectbox_group)
        selectBtn.setOnClickListener {
            val bottomSheet = CategoryBottomSheetFragment()
            val bundle = Bundle().apply {
                putString("current_selected_name", selectBoxTv.text.toString())
            }
            bottomSheet.arguments = bundle
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }


        parentFragmentManager.setFragmentResultListener(
            "category_select_request",
            viewLifecycleOwner
        ) { _, bundle ->
            val selectedName = bundle.getString("selectedName") ?: return@setFragmentResultListener
            val now = bundle.getBoolean("now")
            val selectedId = bundle.getInt("selectedId", -1)

            // 선택한 이름 텍스트 갱신
            selectBoxTv.text = selectedName

            // id가 유효하면 다시 API 호출
            if (selectedId != -1) {
                callCategoryApi(selectedId)
            }
        }
    }

    private fun callCategoryApi(categoryId: Int) {
        RetrofitClient.instance.getChartItems(categoryId).enqueue(object :
            Callback<List<ChartItem>> {
            override fun onResponse(
                call: Call<List<ChartItem>>,
                response: Response<List<ChartItem>>
            ) {
                if (response.isSuccessful) {
                    val chartItems = response.body() ?: emptyList()
                    val adapter = ChartAdapter(requireContext(), chartItems, userId, apiService)
                    listView.adapter = adapter
                    Log.w("ChartAPI", "응답 성공 ${response.body()}")
                } else {
                    Log.e("ChartAPI", "응답 실패 - code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<ChartItem>>, t: Throwable) {
                Log.e("ChartAPI", "❌ 실패: ${t.message}", t)
            }
        })
    }

}