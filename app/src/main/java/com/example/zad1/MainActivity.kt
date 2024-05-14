package com.example.zad1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.myToolbar))
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val test: ArrayList<Trail> = arrayListOf<Trail>()
        test.add(Trail("Trail 1", "Easy", "Very nice path", 60))
        test.add(Trail("Trail 2", "Medium", "Very nice path", 30))
        test.add(Trail("Trail 3", "Hard", "Very nice path", 4))
        test.add(Trail("Trail 4", "Very Hard", "Very nice path", 120))

        val frag = FragmentList.newInstance(test)

        supportFragmentManager.beginTransaction()
            .replace(R.id.pathList, frag)
            .commit()
    }
}