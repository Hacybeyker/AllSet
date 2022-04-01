package com.hacybeyker.allset.view.notifications.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hacybeyker.allset.R
import com.hacybeyker.allset.databinding.RecyclerNotificationChannelBinding
import com.hacybeyker.allset.view.notifications.vo.NotificationVO

/**
 * Created by Carlos Osorio on 28/06/2021
 */

class NotificationChannelAdapter :
    RecyclerView.Adapter<NotificationChannelAdapter.NotificationChannelAdapterViewHolder>() {

    var items = arrayListOf<NotificationVO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationChannelAdapterViewHolder {
        return NotificationChannelAdapterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NotificationChannelAdapterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class NotificationChannelAdapterViewHolder(
        private val binding: RecyclerNotificationChannelBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): NotificationChannelAdapterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    RecyclerNotificationChannelBinding.inflate(layoutInflater, parent, false)
                return NotificationChannelAdapterViewHolder(binding)
            }
        }

        fun bind(item: NotificationVO) {
            with(binding) {
                tvItemNotificationId.text = item.notificationId
                tvItemNotificationName.text = item.notificationName
                tvItemNotificationStatus.text = item.getImportanceText()
                //https://stackoverflow.com/questions/41098015/how-to-set-vectordrawable-as-an-image-for-imageview-programmatically
                when (item.notificationImportance) {
                    0 -> ivItemNotificationStatus.setColorFilter(itemView.context.getColor(R.color.red))
                    else -> ivItemNotificationStatus.setColorFilter(itemView.context.getColor(R.color.green))
                }
            }
        }
    }
}