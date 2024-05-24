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

    private var handler = Handler(Looper.getMainLooper())
    private var isRunning = false

    private val trailViewModel: TrailViewModel by activityViewModels()
    private var trailId: Int = 0
    private var trail: Trail? = null

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

        trail = trailViewModel.getTrailById(trailId).value

        tvTime = view.findViewById(R.id.stopwatchTime)
        btnPlayPause = view.findViewById(R.id.stopwatchPlayButton)
        btnClear = view.findViewById(R.id.stopwatchClearButton)
        trail?.let {
            val now = System.currentTimeMillis()
            val elapsed = now - it.stopwatchStartTime + it.elapsedTime
            val seconds = (elapsed / 1000) % 60
            val minutes = (elapsed / (1000 * 60)) % 60
            val hours = (elapsed / (1000 * 60 * 60)) % 24
            tvTime.text =
                String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        }

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

        trailViewModel.getTrailById(trailId).observe(viewLifecycleOwner, Observer { updatedTrail ->
            trail = updatedTrail
            updatedTrail?.let {
                val currentTime = System.currentTimeMillis()
                val elapsedTime = if (it.stopwatchRunning) {
                    currentTime - it.stopwatchStartTime + it.elapsedTime
                } else {
                    it.elapsedTime
                }
                updateTimerText(elapsedTime)

                if (it.stopwatchRunning) {
                    isRunning = true
                    btnPlayPause.setImageResource(R.drawable.ic_pause_dark)
                    startStopwatch(it.stopwatchStartTime, it.elapsedTime)
                } else {
                    isRunning = false
                    btnPlayPause.setImageResource(R.drawable.ic_play_dark)
                }
            }
        })
    }

    private fun startStopwatch(startTime: Long = System.currentTimeMillis(), elapsedTime: Long = 0L) {
        trail?.let {
            it.stopwatchRunning = true
            it.stopwatchStartTime = startTime
            it.elapsedTime = elapsedTime
            trailViewModel.update(it)
        }
        isRunning = true
        btnPlayPause.setImageResource(R.drawable.ic_pause_dark)
        handler.post(updateRunnable)
    }

    private fun pauseStopwatch() {
        handler.removeCallbacks(updateRunnable)
        trail?.let {
            it.stopwatchRunning = false
            it.elapsedTime = System.currentTimeMillis() - it.stopwatchStartTime + it.elapsedTime
            trailViewModel.update(it)
        }
        isRunning = false
        btnPlayPause.setImageResource(R.drawable.ic_play_dark)
    }

    private fun clearStopwatch() {
        handler.removeCallbacks(updateRunnable)
        trail?.let {
            it.stopwatchRunning = false
            it.stopwatchStartTime = 0L
            it.elapsedTime = 0L
            trailViewModel.update(it)
        }
        isRunning = false
        tvTime.text = "00:00:00"
        btnPlayPause.setImageResource(R.drawable.ic_play_dark)
    }

    private val updateRunnable = object : Runnable {
        override fun run() {
            trail?.let {
                val now = System.currentTimeMillis()
                val elapsed = now - it.stopwatchStartTime + it.elapsedTime
                updateTimerText(elapsed)
                handler.postDelayed(this, 1000)
            }
        }
    }

    private fun updateTimerText(elapsedTime: Long) {
        val seconds = (elapsedTime / 1000) % 60
        val minutes = (elapsedTime / (1000 * 60)) % 60
        val hours = (elapsedTime / (1000 * 60 * 60)) % 24
        tvTime.text = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }
}