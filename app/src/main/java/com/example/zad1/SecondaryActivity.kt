package com.example.zad1

import TrailDetailFragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
                val fab: FloatingActionButton = findViewById(R.id.fab)
                val trailName = trail.name
                supportFragmentManager.beginTransaction()
                    .replace(R.id.trailDescriptionFl, detailFragment)
                    .commit()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.stopwatch, stopwatchFragment)
                    .commit()
                fab.setOnClickListener {
//                    val intent = Intent(Intent.ACTION_SEND)
////                    intent.addCategory(Intent.CATEGORY_APP_MESSAGING)
////                    intent.putExtra("sms_body", "Test");
//                    startActivity(intent)
                    val sendIntent = Intent(Intent.ACTION_VIEW)
                    sendIntent.setData(Uri.parse("sms:"))
                    sendIntent.putExtra("sms_body", "Greetings from $trailName! :)");
                    startActivity(sendIntent);
                }
            }
        }
    }

    private fun openMessagingApp(trail: String) {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_APP_MESSAGING)
            data = Uri.parse("sms:")
            putExtra("sms_body", "Hello, greetings from trail $trail!")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}