package com.example.zad1

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrailAdapter : RecyclerView.Adapter<TrailAdapter.TrailViewHolder>() {

    private var trails: List<Trail> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_list_item, parent, false)
        return TrailViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrailViewHolder, position: Int) {
        val trail = trails[position]
        holder.bind(trail)
    }

    override fun getItemCount(): Int = trails.size

    fun setTrails(trails: List<Trail>) {
        this.trails = trails
        notifyDataSetChanged()
    }

    class TrailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trailName: TextView = itemView.findViewById(R.id.trailName)
        private val cardView: CardView = itemView.findViewById(R.id.cardView)
        private val trailImage: ImageView = itemView.findViewById(R.id.trailImage)


        fun bind(trail: Trail) {
            trailName.text = trail.name
            Glide.with(itemView.context)
                .load(trail.imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .into(trailImage)

            cardView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, SecondaryActivity::class.java).apply {
                    putExtra("trail", trail)
                }
                context.startActivity(intent)
            }
        }
    }
}