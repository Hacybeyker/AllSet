package com.hacybeyker.allset.view.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hacybeyker.allset.data.Movie
import com.hacybeyker.allset.databinding.RecyclerItemMovieBinding
import com.hacybeyker.allset.utils.extensions.imageLoad

/**
 * Created by Carlos Osorio on 16/06/2021
 */

class RecyclerMainAdapter :
    RecyclerView.Adapter<RecyclerMainAdapter.RecyclerMainAdapterViewHolder>() {

    var items = arrayListOf<Movie>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerMainAdapterViewHolder {
        return RecyclerMainAdapterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerMainAdapterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class RecyclerMainAdapterViewHolder(
        private val binding: RecyclerItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): RecyclerMainAdapterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerItemMovieBinding.inflate(layoutInflater, parent, false)
                return RecyclerMainAdapterViewHolder(binding)
            }
        }

        fun bind(item: Movie) {
            with(binding) {
                this.itemMovieTitle.text = item.title
                this.itemMoviePosterImage.imageLoad(url = item.posterImage)
            }
        }
    }
}