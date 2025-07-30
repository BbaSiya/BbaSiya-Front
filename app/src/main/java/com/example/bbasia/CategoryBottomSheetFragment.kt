package com.example.bbasia

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var listView: ListView
    private lateinit var apiService: ApiService
    private var currentSelectedName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.category_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = view.findViewById(R.id.category_bottom_sheet_lv)
        apiService = RetrofitClient.instance

        currentSelectedName = arguments?.getString("current_selected_name")


        apiService.getCategories().enqueue(object : Callback<List<CategoryItem>> {
            override fun onResponse(
                call: Call<List<CategoryItem>>,
                response: Response<List<CategoryItem>>
            ) {
                if (response.isSuccessful) {
                    val categories = response.body() ?: emptyList()
                    val categoryBottomSheetItems = categories.mapIndexed { index, item ->
                        val cleanName = item.name.replace("\\n", " ")
                        CategoryBottomSheetItem(
                            name = cleanName,
                            now = cleanName == currentSelectedName,
                            new = (index == 0)   // 첫번째 항목만 new=true
                        )
                    }

                    val adapter = CategoryBottomSheetAdapter(requireContext(), categoryBottomSheetItems)
                    listView.adapter = adapter

                    listView.setOnItemClickListener { _, _, position, _ ->
                        val selectedItem = categoryBottomSheetItems[position]

                        val updatedItem = selectedItem.copy(now = true)

                        val result = Bundle().apply {
                            putString("selectedName", selectedItem.name)
                            putInt("selectedId", categories[position].id)
                        }

                        parentFragmentManager.setFragmentResult("category_select_request", result)
                        dismiss()
                    }
                } else {
                    Log.e("CategoryBottomSheet", "응답 실패 - code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<CategoryItem>>, t: Throwable) {
                Log.e("CategoryBottomSheet", "❌ 실패: ${t.message}", t)
            }
        })

        val cancelBtn = view.findViewById<ImageView>(R.id.category_bottom_sheet_cancel_btn)
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }
}