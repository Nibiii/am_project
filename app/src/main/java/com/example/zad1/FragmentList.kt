package com.example.zad1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class FragmentList : Fragment() {
    private val listItems: ArrayList<FragmentListItem> = arrayListOf<FragmentListItem>()

    companion object {
        private const val LIST_ITEMS = "List Items"
        fun newInstance(listItems: ArrayList<Trail>) = FragmentListItem().apply {
            val helper = arrayListOf<FragmentListItem>()
            val args = Bundle()
            for (item in listItems) {
                val fragmentListItem = FragmentListItem.newInstance(item.name, item.difficulty)
                helper.add(fragmentListItem)
            }
            args.putParcelableArrayList("listItems", helper)
            arguments = bundleOf(
                LIST_ITEMS to helper
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (item in listItems) {
            childFragmentManager.beginTransaction().add(R.id.layoutFragmentList, item).commit()
        }
    }

    fun addItem(listItem: FragmentListItem){
        listItems.add(listItem)
    }

    fun removeItemByTitle(title: String){}
}