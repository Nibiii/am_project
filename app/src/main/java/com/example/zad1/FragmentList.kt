package com.example.zad1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager

class TrailList : Fragment() {

    private val trailViewModel: TrailViewModel by activityViewModels()
    private lateinit var trailAdapter: TrailAdapter
    private val fragmentKey: String
        get() = requireArguments().getString(FRAGMENT_KEY)
            ?: throw IllegalArgumentException("Argument $FRAGMENT_KEY required")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trailAdapter = TrailAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = trailAdapter

        trailViewModel.allTrails.observe(viewLifecycleOwner, Observer { trails ->
            trails?.let {
                val helper = ArrayList<Trail>()
                for (trail in it) {
                    if (trail.difficulty.lowercase() == fragmentKey)
                        helper.add(trail)
                }
                trailAdapter.setTrails(helper)
            }
        })
    }

    companion object {
        private const val FRAGMENT_KEY = "fragmentKey"

        fun newInstance(fragmentKey: String) = TrailList().apply {
            arguments = bundleOf(
                FRAGMENT_KEY to fragmentKey.lowercase()
            )
        }
    }
}
