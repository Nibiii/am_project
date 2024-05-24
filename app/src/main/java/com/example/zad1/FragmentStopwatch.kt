package com.example.zad1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import java.util.Locale

class StopwatchFragment : Fragment() {

    private lateinit var tvTime: TextView
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnClear: ImageButton
    private lateinit var tvTrailInfo: TextView

    private var handler = Handler(Looper.getMainLooper())
    private var startTime = 0L
    private var elapsedTime = 0L
    private var isRunning = false

    private val trailViewModel: TrailViewModel by activityViewModels()
    private var trailId: Int = 0

    companion object {
        private const val ARG_TRAIL_ID = "trail_id"

        fun newInstance(trailId: Int): StopwatchFragment {
            val fragment = StopwatchFragment()
            val args = Bundle()
            args.putInt(ARG_TRAIL_ID, trailId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trailId = it.getInt(ARG_TRAIL_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stopwatch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTime = view.findViewById(R.id.stopwatchTime)
        btnPlayPause = view.findViewById(R.id.stopwatchPlayButton)
        btnClear = view.findViewById(R.id.stopwatchClearButton)

        btnPlayPause.setOnClickListener {
            if (isRunning) {
                pauseStopwatch()
            } else {
                startStopwatch()
            }
        }

        btnClear.setOnClickListener {
            clearStopwatch()
        }

        trailViewModel.getTrailById(trailId).observe(viewLifecycleOwner, Observer { trail ->
            trail?.let {
                tvTrailInfo.text = "${it.name} - ${it.difficulty}"
                startTime = it.stopwatchStartTime
                isRunning = it.stopwatchRunning
                if (isRunning) {
                    startStopwatch()
                } else {
                    pauseStopwatch()
                }
            }
        })
    }

    private fun startStopwatch() {
        startTime = System.currentTimeMillis()
        handler.post(updateRunnable)
        isRunning = true
        btnPlayPause.setImageResource(R.drawable.ic_pause_dark)
        updateTrail()
    }

    private fun pauseStopwatch() {
        handler.removeCallbacks(updateRunnable)
        elapsedTime += System.currentTimeMillis() - startTime
        isRunning = false
        btnPlayPause.setImageResource(R.drawable.ic_play_dark)
        updateTrail()
    }

    private fun clearStopwatch() {
        handler.removeCallbacks(updateRunnable)
        startTime = 0L
        elapsedTime = 0L
        isRunning = false
        tvTime.text = "00:00:00"
        btnPlayPause.setImageResource(R.drawable.ic_play_dark)
        updateTrail()
    }

    private val updateRunnable = object : Runnable {
        override fun run() {
            val now = System.currentTimeMillis()
            val timeElapsed = now - startTime + elapsedTime
            val seconds = (timeElapsed / 1000) % 60
            val minutes = (timeElapsed / (1000 * 60)) % 60
            val hours = (timeElapsed / (1000 * 60 * 60)) % 24
            tvTime.text = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
            handler.postDelayed(this, 1000)
        }
    }

    private fun updateTrail() {
        val updatedTrail = trailViewModel.getTrailById(trailId).value?.apply {
            stopwatchRunning = isRunning
            stopwatchStartTime = if (isRunning) startTime else 0L
        }
        updatedTrail?.let { trailViewModel.update(it) }
    }
}