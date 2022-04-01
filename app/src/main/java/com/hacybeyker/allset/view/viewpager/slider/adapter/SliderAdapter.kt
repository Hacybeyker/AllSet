package com.hacybeyker.allset.view.viewpager.slider.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hacybeyker.allset.data.Movie
import com.hacybeyker.allset.databinding.RecyclerViewPagerSliderBinding
import com.hacybeyker.allset.utils.extensions.imageLoad

/**
 * Created by Carlos Osorio on 25/06/2021
 */

class SliderAdapter :
    RecyclerView.Adapter<SliderAdapter.SliderAdapterViewHolder>() {

    var items = arrayListOf<Movie>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SliderAdapterViewHolder {
        return SliderAdapterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SliderAdapterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class SliderAdapterViewHolder(
        private val binding: RecyclerViewPagerSliderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): SliderAdapterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerViewPagerSliderBinding.inflate(layoutInflater, parent, false)
                return SliderAdapterViewHolder(binding)
            }
        }

        fun bind(item: Movie) {
            with(binding) {
                rivPoster.imageLoad(item.posterImage)
            }
        }
    }
}