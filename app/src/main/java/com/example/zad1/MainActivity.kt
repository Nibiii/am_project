package com.example.zad1

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.Observer


class MainActivity : AppCompatActivity() {

    private val trailViewModel: TrailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load the TrailListFragment when the activity is created
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.pathList, FragmentList())
            }
        }
    }
}