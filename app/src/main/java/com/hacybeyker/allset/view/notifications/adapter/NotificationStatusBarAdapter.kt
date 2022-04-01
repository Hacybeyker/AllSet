package com.hacybeyker.allset.view.notifications.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hacybeyker.allset.databinding.RecyclerNotificationStatusBarBinding

/**
 * Created by Carlos Osorio on 28/06/2021
 */

class NotificationStatusBarAdapter :
    RecyclerView.Adapter<NotificationStatusBarAdapter.NotificationStatusBarAdapterViewHolder>() {

    var items = arrayListOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationStatusBarAdapterViewHolder {
        return NotificationStatusBarAdapterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NotificationStatusBarAdapterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class NotificationStatusBarAdapterViewHolder(
        private val binding: RecyclerNotificationStatusBarBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): NotificationStatusBarAdapterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    RecyclerNotificationStatusBarBinding.inflate(layoutInflater, parent, false)
                return NotificationStatusBarAdapterViewHolder(binding)
            }
        }

        fun bind(item: String) {
            with(binding) {
                tvItemNotificationId.text = item
            }
        }
    }
}