package com.example.zad1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager

class FragmentList : Fragment(), OnTrailClickListener {

    private val trailViewModel: TrailViewModel by activityViewModels()
    private lateinit var trailAdapter: TrailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val trailList = listOf(
//            Trail("Trail 1", "Easy", "Very nice path", 60),
//            Trail("Trail 2", "Medium", "Very nice path", 30),
//            Trail("Trail 3", "Hard", "Very nice path", 4),
//            Trail("Trail 4", "Very Hard", "Very nice path", 120),
//            Trail("Trail 5", "Easy", "Scenic route with mild inclines", 45),
//            Trail("Trail 6", "Medium", "Moderately challenging with rocky terrain", 50),
//            Trail("Trail 7", "Hard", "Steep ascents and rugged path", 20),
//            Trail("Trail 8", "Very Hard", "Extremely challenging with steep climbs", 150),
//            Trail("Trail 9", "Easy", "Pleasant walk through forest", 70),
//            Trail("Trail 10", "Medium", "Balanced trail with some difficult parts", 40),
//            Trail("Trail 11", "Hard", "Intense trail for experienced hikers", 35),
//            Trail("Trail 12", "Very Hard", "Demanding trail with high altitudes", 180),
//            Trail("Trail 13", "Easy", "Flat and leisurely walk", 25),
//            Trail("Trail 14", "Medium", "Mixture of easy and challenging sections", 55),
//            Trail("Trail 15", "Hard", "Requires good physical condition", 60),
//            Trail("Trail 16", "Very Hard", "Expert level trail with rough terrain", 200)
//        )

        Log.d("info", "odpaloned")
        trailAdapter = TrailAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = trailAdapter

        trailViewModel.allTrails.observe(viewLifecycleOwner, Observer { trails ->
            trails?.let { trailAdapter.setTrails(it) }
        })

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            (recyclerView.layoutManager as LinearLayoutManager).orientation
        )
//        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    override fun onTrailClick(trail: Trail) {
        // Handle the trail click event here
        Toast.makeText(requireContext(), "Clicked: ${trail.name}", Toast.LENGTH_SHORT).show()
    }
}
