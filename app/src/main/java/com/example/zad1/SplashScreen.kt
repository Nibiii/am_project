package com.example.zad1

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator


@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val colorValues = intArrayOf(
            Color.RED, Color.GREEN, Color.BLUE, Color.RED
        )
        val image: ImageView = findViewById(R.id.SplashScreenImage)
        val colorAnimator = ObjectAnimator.ofInt(image.drawable, "tint", *colorValues)
        colorAnimator.setEvaluator(ArgbEvaluator())
        colorAnimator.duration = 5000
        colorAnimator.repeatCount = ObjectAnimator.INFINITE
        colorAnimator.repeatMode = ObjectAnimator.REVERSE

        colorAnimator.start()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}