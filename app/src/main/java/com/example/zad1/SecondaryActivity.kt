package com.example.zad1

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class SecondaryActivity : CustomActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_secondary)
        super.onCreate(savedInstanceState)

        val trail: Trail? = intent.getParcelableExtra("trail")

        if (savedInstanceState == null) {
            trail?.let {
                val toolbar: Toolbar = findViewById(R.id.toolbar)
                val detailFragment = TrailDetailFragment.newInstance(it)
                val stopwatchFragment = StopwatchFragment.newInstance(it.id)
                val fab: FloatingActionButton = findViewById(R.id.fab)
                val trailName = trail.name

                Glide.with(this).asDrawable().load(trail.imageUrl)
                    .into(object : CustomTarget<Drawable?>() {
                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable?>?
                        ) {
                            toolbar.background = resource
                        }
                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
                supportFragmentManager.beginTransaction()
                    .replace(R.id.trailDescriptionFl, detailFragment)
                    .commit()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.stopwatch, stopwatchFragment)
                    .commit()
                fab.setOnClickListener {
                    val sendIntent = Intent(Intent.ACTION_VIEW)
                    sendIntent.setData(Uri.parse("sms:"))
                    sendIntent.putExtra("sms_body", "Greetings from $trailName! :)");
                    startActivity(sendIntent);
                }
            }
        }
    }
}