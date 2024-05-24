package com.example.zad1

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView

//class TrailAdapter(
//    private val trails: List<Trail>,
//    private val listener: OnTrailClickListener) :
//    RecyclerView.Adapter<TrailAdapter.TrailViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_list_item, parent, false)
//        return TrailViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: TrailViewHolder, position: Int) {
//        val trail = trails[position]
//        holder.bind(trail, listener)
//    }
//
//    override fun getItemCount() = trails.size
//
//    class TrailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val nameTextView: TextView = itemView.findViewById(R.id.trailName)
//        private val descriptionTextView: TextView = itemView.findViewById(R.id.trailDescription)
//
//        fun bind(trail: Trail, listener: OnTrailClickListener) {
//            nameTextView.text = trail.name
//            descriptionTextView.text = trail.description
//
//            itemView.setOnClickListener {
//                val startColor = ContextCompat.getColor(itemView.context, android.R.color.transparent)
//                val endColor = ContextCompat.getColor(itemView.context, android.R.color.darker_gray)
//                animateBackgroundColor(itemView, startColor, endColor, 150L)
//
////                listener.onTrailClick(trail)
//                val context = itemView.context
//                val intent = Intent(context, SecondaryActivity::class.java).apply {
//                    putExtra("trail", trail)
//                }
//                context.startActivity(intent)
//            }
//        }
//    }
//}

fun animateBackgroundColor(view: View, startColor: Int, endColor: Int, duration: Long) {
    val colorAnimatorForward = ObjectAnimator.ofArgb(view, "backgroundColor", startColor, endColor)
    val colorAnimatorReverse = ObjectAnimator.ofArgb(view, "backgroundColor", endColor, startColor)

    val animatorSet = AnimatorSet()
    animatorSet.playSequentially(colorAnimatorForward, colorAnimatorReverse)
    animatorSet.duration = duration
    animatorSet.start()
}

interface OnTrailClickListener {
    fun onTrailClick(trail: Trail)
}