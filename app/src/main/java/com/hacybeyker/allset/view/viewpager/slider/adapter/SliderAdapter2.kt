package com.hacybeyker.allset.view.viewpager.slider.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hacybeyker.allset.data.Movie
import com.hacybeyker.allset.databinding.RecyclerViewPagerSlider2Binding
import com.hacybeyker.allset.utils.extensions.imageLoad

/**
 * Created by Carlos Osorio on 25/06/2021
 */

//typealias onHelouda = (Runnable) -> Unit
//val onHelouda: (Runnable) -> Unit

class SliderAdapter2() :
    RecyclerView.Adapter<SliderAdapter2.SliderAdapterViewHolder>() {

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
        private val binding: RecyclerViewPagerSlider2Binding
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): SliderAdapterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerViewPagerSlider2Binding.inflate(layoutInflater, parent, false)
                return SliderAdapterViewHolder(binding)
            }
        }

        fun bind(item: Movie) {
            with(binding) {
                rivPoster.imageLoad(item.landscapeImage)
                tvTittle.text = item.title
            }
        }
    }
}