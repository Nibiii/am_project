package com.example.zad1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf

class FragmentListItem : Fragment(R.layout.fragment_list_item) {
    private val title: String = ""
    private val difficulty: String = ""
//    private val title: String
//        get() = requireArguments().getString(TITLE)
//            ?: throw IllegalArgumentException("Argument $TITLE required")
//
//    private val difficulty: String
//        get() = requireArguments().getString(DIFFICULTY)
//            ?: throw IllegalArgumentException("Argument $DIFFICULTY required")
    companion object {
        private const val TITLE = "title"
        private const val DIFFICULTY = "difficulty"
        fun newInstance(title: String, difficulty: String) = FragmentListItem().apply {
            arguments = bundleOf(
                TITLE to title,
                DIFFICULTY to difficulty
            )
            return this
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemTitle = view.findViewById<TextView>(R.id.listItemTitle)
        val itemDifficulty = view.findViewById<TextView>(R.id.listItemDifficulty)
        itemTitle.text = title
        itemDifficulty.text = difficulty
    }
}