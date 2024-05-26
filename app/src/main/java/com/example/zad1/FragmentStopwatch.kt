package com.example.zad1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
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

        lifecycleScope.launch {
            trail = trailViewModel.getTrailByIdSuspend(trailId)
            trail?.let {
                Log.d("StopwatchFragment", "Fetched trail: ${it.stopwatchRunning}")
                updateUI(it)
                if (it.stopwatchRunning)
                    startStopwatch(it.stopwatchStartTime, it.elapsedTime)
            }

            // Set up the observer to update the UI when the Trail data changes
            trailViewModel.getTrailById(trailId).observe(viewLifecycleOwner, Observer { updatedTrail ->
                trail = updatedTrail
                updatedTrail?.let {
                    Log.d("StopwatchFragment", "Trail updated: ${it.stopwatchRunning}")
                    updateUI(it)
                }
            })
        }


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
    }

    private fun updateUI(trail: Trail) {
        val currentTime = System.currentTimeMillis()
        val elapsedTime = if (trail.stopwatchRunning) {
            currentTime - trail.stopwatchStartTime + trail.elapsedTime
        } else {
            trail.elapsedTime
        }
        updateTimerText(elapsedTime)

        if (trail.stopwatchRunning) {
            isRunning = true
            btnPlayPause.setImageResource(R.drawable.ic_pause_dark)
//            startStopwatch(trail.stopwatchStartTime, trail.elapsedTime)
        } else {
            isRunning = false
            btnPlayPause.setImageResource(R.drawable.ic_play_dark)
        }
    }

    private fun startStopwatch(startTime: Long = System.currentTimeMillis(), elapsedTime: Long = 0L) {
        Log.d("StopwatchFragment", "Starting stopwatch from $startTime with elapsed $elapsedTime")
        btnPlayPause.setImageResource(R.drawable.ic_pause_dark)
        trail?.let {
            it.stopwatchRunning = true
            it.stopwatchStartTime = startTime - it.elapsedTime
            it.elapsedTime = elapsedTime
            lifecycleScope.launch {
                try {
                    trailViewModel.update(it)
                } catch (e: Exception) {
                    Log.e("StopwatchFragment", "Error updating trail: ${e.message}")
                }
            }
        }
        isRunning = true
        handler.post(updateRunnable)
    }

    private fun pauseStopwatch() {
        Log.d("StopwatchFragment", "test stopwatch")
        btnPlayPause.setImageResource(R.drawable.ic_play_dark)
        handler.removeCallbacks(updateRunnable)
        handler.removeCallbacksAndMessages(null)
        isRunning = false
        trail?.let {
            it.stopwatchRunning = false
            it.elapsedTime = System.currentTimeMillis() - it.stopwatchStartTime
            lifecycleScope.launch {
                try {
                    trailViewModel.update(it)
                } catch (e: Exception) {
                    Log.e("StopwatchFragment", "Error updating trail: ${e.message}")
                }
            }
        }
        Log.d("StopwatchFragment", "Paused stopwatch")
    }

    private fun clearStopwatch() {
        handler.removeCallbacks(updateRunnable)
        handler.removeCallbacksAndMessages(null)
        isRunning = false
        trail?.let {
            it.stopwatchRunning = false
            it.stopwatchStartTime = 0L
            it.elapsedTime = 0L
            lifecycleScope.launch {
                try {
                    trailViewModel.update(it)
                } catch (e: Exception) {
                    Log.e("StopwatchFragment", "Error updating trail: ${e.message}")
                }
            }
        }
        tvTime.text = "00:00:00"
        btnPlayPause.setImageResource(R.drawable.ic_play_dark)
        Log.d("StopwatchFragment", "Cleared stopwatch")
    }



    private val updateRunnable = object : Runnable {
        override fun run() {
//                Log.d("StopwatchFragment", "Updating timer text")
            if (!isRunning)
                return
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