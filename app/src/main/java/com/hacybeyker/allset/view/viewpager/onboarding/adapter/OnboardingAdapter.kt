package com.hacybeyker.allset.view.viewpager.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hacybeyker.allset.data.Movie
import com.hacybeyker.allset.databinding.ItemViewPagerOnboardingBinding
import com.hacybeyker.allset.utils.extensions.imageLoad

/**
 * Created by Carlos Osorio on 24/06/2021
 */

class OnboardingAdapter :
    RecyclerView.Adapter<OnboardingAdapter.RecyclerOnboardingAdapterViewHolder>() {

    var items = arrayListOf<Movie>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerOnboardingAdapterViewHolder {
        return RecyclerOnboardingAdapterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerOnboardingAdapterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class RecyclerOnboardingAdapterViewHolder(
        private val binding: ItemViewPagerOnboardingBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): RecyclerOnboardingAdapterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemViewPagerOnboardingBinding.inflate(layoutInflater, parent, false)
                return RecyclerOnboardingAdapterViewHolder(binding)
            }
        }

        fun bind(item: Movie) {
            with(binding) {
                ivPoster.imageLoad(item.posterImage)
                tvTitle.text = item.title
                tvDescription.text = item.description
            }
        }
    }
}