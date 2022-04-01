package com.hacybeyker.allset.principal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ItemOptionBinding

class MainAdapter(private val onItemSelectedListener: OnItemSelectedListener) :
    RecyclerView.Adapter<MainAdapter.MainAdapterViewHolder>() {

    var items: List<Item> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainAdapterViewHolder {
        return MainAdapterViewHolder.from(parent, onItemSelectedListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MainAdapterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class MainAdapterViewHolder(
        private val binding: ItemOptionBinding,
        private val onItemSelectedListener: OnItemSelectedListener
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(
                parent: ViewGroup,
                onItemSelectedListener: OnItemSelectedListener
            ): MainAdapterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemOptionBinding.inflate(layoutInflater, parent, false)
                return MainAdapterViewHolder(binding, onItemSelectedListener)
            }
        }

        fun bind(item: Item) {
            with(binding) {
                this.item = item
                itemSelected = onItemSelectedListener
                executePendingBindings()
            }
        }
    }

    interface OnItemSelectedListener {
        fun onItemSelected(item: Item)
    }
}