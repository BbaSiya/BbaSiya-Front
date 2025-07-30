package com.example.bbasia

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class MainFragment : Fragment() {
    private val categoryFragment = CategoryFragment()
    private lateinit var goodBtn: ImageButton
    private lateinit var backBtn: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.main_fl, categoryFragment)
                .commit()
        }

        val infoButton = view.findViewById<ImageButton>(R.id.main_info_btn)
        infoButton.setOnClickListener {
            val bottomSheet = InfoBottomSheetFragment()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

        goodBtn = view.findViewById(R.id.main_like_btn)
        // 프래그먼트 변경 시마다 visibility 체크
        childFragmentManager.addOnBackStackChangedListener {
            updateGoodBtnVisibility()
        }

        // 초기 상태도 반영
        updateGoodBtnVisibility()


        backBtn = view.findViewById(R.id.main_back_btn)
        backBtn.setOnClickListener {
            val currentFragment = childFragmentManager.findFragmentById(R.id.main_fl)
            if (currentFragment is CategoryDetailFragment) {
                // CategoryDetailFragment면 이전 프래그먼트로 돌아가기
                childFragmentManager.popBackStack()
            } else {
                // 아니면 Activity 종료 또는 다른 처리
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun updateGoodBtnVisibility() {
        val currentFragment = childFragmentManager.findFragmentById(R.id.main_fl)
        goodBtn.visibility = when (currentFragment) {
            is CategoryDetailFragment -> View.VISIBLE
            is CategoryFragment -> View.GONE
            else -> View.GONE
        }
    }
}
