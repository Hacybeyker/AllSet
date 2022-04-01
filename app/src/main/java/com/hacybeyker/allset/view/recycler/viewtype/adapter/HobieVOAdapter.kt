package com.hacybeyker.allset.view.recycler.viewtype.adapter;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hacybeyker.allset.databinding.RecyclerItemViewTypeListItemBinding
import com.hacybeyker.allset.view.recycler.viewtype.vo.HobieVO

class HobieVOAdapter(private val onItemSelectedListener: OnItemSelectedListener) :
    RecyclerView.Adapter<HobieVOAdapter.HobieVOViewHolder>() {

    var items: List<HobieVO> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HobieVOViewHolder {
        return HobieVOViewHolder.from(parent, onItemSelectedListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: HobieVOViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class HobieVOViewHolder(
        private val binding: RecyclerItemViewTypeListItemBinding,
        private val onItemSelectedListener: OnItemSelectedListener
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(
                parent: ViewGroup,
                onItemSelectedListener: OnItemSelectedListener
            ): HobieVOViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    RecyclerItemViewTypeListItemBinding.inflate(layoutInflater, parent, false)
                return HobieVOViewHolder(binding, onItemSelectedListener)
            }
        }

        fun bind(item: HobieVO) {
            binding.tvTitleHobie.text = item.name
        }
    }

    interface OnItemSelectedListener {
        fun onItemSelected(item: HobieVO)
    }
}