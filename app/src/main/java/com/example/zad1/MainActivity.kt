package com.example.zad1

import android.os.Bundle
import com.example.zad1.databinding.ActivityMainBinding


class MainActivity : CustomActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(FragmentHome(), "Home")
        adapter.addFragment(TrailList.newInstance("easy"), "Easy")
        adapter.addFragment(TrailList.newInstance("medium"), "Medium")
        adapter.addFragment(TrailList.newInstance("hard"), "Hard")

        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        super.onCreate(savedInstanceState)
    }
}