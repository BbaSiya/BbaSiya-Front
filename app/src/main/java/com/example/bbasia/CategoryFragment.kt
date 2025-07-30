package com.example.bbasia

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryFragment : Fragment() {

    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.category_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = view.findViewById(R.id.category_lv)

        RetrofitClient.instance.getCategories().enqueue(object : Callback<List<CategoryItem>> {
            override fun onResponse(
                call: Call<List<CategoryItem>>,
                response: Response<List<CategoryItem>>
            ) {
                if (response.isSuccessful) {
                    val categoryItems = response.body() ?: emptyList()
                    val adapter = CategoryAdapter(requireContext(), categoryItems)
                    listView.adapter = adapter

                    listView.setOnItemClickListener { _, _, position, _ ->
                        val selectedItem = categoryItems[position]

                        val detailFragment = CategoryDetailFragment()
                        val bundle = Bundle().apply {
                            putString("category_name", selectedItem.name)
                            putInt("category_id", selectedItem.id)
                            putString("category_description", selectedItem.description)
                        }
                        detailFragment.arguments = bundle

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.main_fl, detailFragment)
                            .addToBackStack("CategoryDetail")
                            .commit()
                    }

                    Log.d("Categoryyy", "응답 크기: ${categoryItems.size}")
                    Log.d("Categoryyy", "내용: $categoryItems")
                } else {
                    Log.e("Categoryyy", "응답 실패 - code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<CategoryItem>>, t: Throwable) {
                // 실패 처리 (예: 토스트 띄우기)
                Log.e("Categoryyy", "❌ 실패: ${t.message}", t)
            }
        })
    }
}