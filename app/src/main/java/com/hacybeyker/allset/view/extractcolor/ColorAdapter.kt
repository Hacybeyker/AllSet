package com.hacybeyker.allset.view.extractcolor;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hacybeyker.allset.databinding.RecyclerItemColorsBinding

class ColorAdapter(private val onItemSelectedListener: OnItemSelectedListener) :
    RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    var items: List<ColorVO> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder.from(parent, onItemSelectedListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ColorViewHolder(
        private val binding: RecyclerItemColorsBinding,
        private val onItemSelectedListener: OnItemSelectedListener
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(
                parent: ViewGroup,
                onItemSelectedListener: OnItemSelectedListener
            ): ColorViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerItemColorsBinding.inflate(layoutInflater, parent, false)
                return ColorViewHolder(binding, onItemSelectedListener)
            }
        }

        fun bind(item: ColorVO) {
            binding.tvTypeColor.text = item.description
            binding.tvColor.text = item.colorHex
            binding.mbApply.text = item.type
            binding.mbApply.setOnClickListener { onItemSelectedListener.onItemSelected(item = item) }
        }
    }

    interface OnItemSelectedListener {
        fun onItemSelected(item: ColorVO)
    }
}