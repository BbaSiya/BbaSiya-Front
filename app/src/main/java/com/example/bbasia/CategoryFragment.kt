package com.example.bbasia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment

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

        val categoryItems = listOf(
            CategoryItem("초고수가 선택한", "중국(홍콩) 종목은?", R.drawable.ic_launcher_background),
            CategoryItem("초고수가 선택한", "중국(홍콩) 종목은?", R.drawable.ic_launcher_background),
            CategoryItem("초고수가 선택한", "중국(홍콩) 종목은?", R.drawable.ic_launcher_background),
            CategoryItem("초고수가 선택한", "중국(홍콩) 종목은?", R.drawable.ic_launcher_background),
            CategoryItem("초고수가 선택한", "중국(홍콩) 종목은?", R.drawable.ic_launcher_background),
            CategoryItem("초고수가 선택한", "중국(홍콩) 종목은?", R.drawable.ic_launcher_background),
            CategoryItem("초고수가 선택한", "중국(홍콩) 종목은?", R.drawable.ic_launcher_background),
            CategoryItem("초고수가 선택한", "중국(홍콩) 종목은?", R.drawable.ic_launcher_background),
            CategoryItem("초고수가 선택한", "중국(홍콩) 종목은?", R.drawable.ic_launcher_background),
            CategoryItem("초고수가 선택한", "중국(홍콩) 종목은?", R.drawable.ic_launcher_background),
            CategoryItem("초고수가 선택한", "중국(홍콩) 종목은?", R.drawable.ic_launcher_background),
            CategoryItem("초고수가 선택한", "중국(홍콩) 종목은?", R.drawable.ic_launcher_background),

        )

        val adapter = CategoryAdapter(requireContext(), categoryItems)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = categoryItems[position]

            val detailFragment = CategoryDetailFragment()

            val bundle = Bundle().apply {
                putString("name1", selectedItem.name1)
                putString("name1", selectedItem.name2)
                putInt("imageResId", selectedItem.imageResId)
            }
            detailFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fl, detailFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}