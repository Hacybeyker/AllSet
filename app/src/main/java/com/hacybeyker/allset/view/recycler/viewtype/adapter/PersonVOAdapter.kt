package com.hacybeyker.allset.view.recycler.viewtype.adapter;

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hacybeyker.allset.databinding.RecyclerItemViewTypeItemBinding
import com.hacybeyker.allset.databinding.RecyclerItemViewTypeListBinding
import com.hacybeyker.allset.view.recycler.viewtype.vo.HobieVO
import com.hacybeyker.allset.view.recycler.viewtype.vo.PersonVO
import com.hacybeyker.allset.view.recycler.viewtype.vo.TypePersonVO

class PersonVOAdapter(private val onItemSelectedListener: OnItemSelectedListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<PersonVO> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        Log.d("TAG", "Here - onCreateViewHolder")
        return when (viewType) {
            1 -> PersonVOItemViewHolder.from(parent, onItemSelectedListener)
            2 -> PersonVOListViewHolder.from(parent, onItemSelectedListener)
            else -> PersonVOItemViewHolder.from(parent, onItemSelectedListener)
        }
    }

    override fun getItemCount(): Int {
        Log.d("TAG", "Here - getItemCount")
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("TAG", "Here - onBindViewHolder: ")
        val item = items[position]
        when (items[position].type) {
            TypePersonVO.NORMAL -> (holder as PersonVOItemViewHolder).bind(item = item)
            TypePersonVO.HARD -> (holder as PersonVOListViewHolder).bind(item = item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        Log.d("TAG", "Here - getItemViewType")
        return when (items[position].type) {
            TypePersonVO.NORMAL -> 1
            TypePersonVO.HARD -> 2
        }
    }

    class PersonVOItemViewHolder(
        private val binding: RecyclerItemViewTypeItemBinding,
        private val onItemSelectedListener: OnItemSelectedListener
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(
                parent: ViewGroup,
                onItemSelectedListener: OnItemSelectedListener
            ): PersonVOItemViewHolder {
                Log.d("TAG", "Here - from PersonVOItemViewHolder")
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerItemViewTypeItemBinding.inflate(layoutInflater, parent, false)
                return PersonVOItemViewHolder(binding = binding, onItemSelectedListener)
            }
        }

        fun bind(item: PersonVO) {
            Log.d("TAG", "Here - bind PersonVOItemViewHolder")
            with(binding) {
                tvTitle.text = item.name
                tvBajada.text = "Tu edad es: ${item.age}"

            }
        }
    }

    class PersonVOListViewHolder(
        private val binding: RecyclerItemViewTypeListBinding,
        private val onItemSelectedListener: OnItemSelectedListener
    ) : RecyclerView.ViewHolder(binding.root), HobieVOAdapter.OnItemSelectedListener {

        val adapter: HobieVOAdapter by lazy { HobieVOAdapter(this) }
        val viewPool : RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

        companion object {
            fun from(
                parent: ViewGroup,
                onItemSelectedListener: OnItemSelectedListener
            ): PersonVOListViewHolder {
                Log.d("TAG", "Here - from PersonVOListViewHolder")
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    RecyclerItemViewTypeListBinding.inflate(layoutInflater, parent, false)
                return PersonVOListViewHolder(binding = binding, onItemSelectedListener)
            }
        }

        fun bind(item: PersonVO) {
            Log.d("TAG", "Here - bind PersonVOListViewHolder")
            with(binding) {
                rvItems.adapter = adapter
                rvItems.setRecycledViewPool(viewPool)
                tvTitle.text = item.name
                tvBajada.text = "Tu edad es: ${item.age}"
                item.hobbies?.let {
                    adapter.items = it
                }
            }
        }

        override fun onItemSelected(item: HobieVO) {
            Log.d("TAG", "Here - onItemSelected: ")
        }
    }

    interface OnItemSelectedListener {
        fun onItemSelected(item: PersonVO)
    }
}