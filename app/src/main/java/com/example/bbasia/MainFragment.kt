package com.example.bbasia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class MainFragment : Fragment() {
    private val categoryFragment = CategoryFragment()

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
    }
}
