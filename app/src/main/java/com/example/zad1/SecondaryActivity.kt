package com.example.zad1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_secondary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val trail: Trail? = intent.getParcelableExtra("trail")

        if (savedInstanceState == null) {
            trail?.let {
                val detailFragment = TrailDetailFragment.newInstance(it)
                val stopwatchFragment = StopwatchFragment.newInstance(it.id)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.trailDescriptionFl, detailFragment)
                    .commit()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.stopwatch, stopwatchFragment)
                    .commit()
            }
        }
    }
}