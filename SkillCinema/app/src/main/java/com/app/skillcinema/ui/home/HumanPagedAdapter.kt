package com.app.skillcinema.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.skillcinema.R
import com.app.skillcinema.data.retrofit.HumanItem
import com.app.skillcinema.databinding.ItemDetailActorRecyclerElementBinding
import com.bumptech.glide.Glide

class HumanPagedAdapter (
    private val onClick: (HumanItem) -> Unit
) : PagingDataAdapter<HumanItem, HumanPagedAdapter.HumanViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HumanViewHolder {
        val binding = ItemDetailActorRecyclerElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HumanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HumanViewHolder, position: Int) {
        val human = getItem(position)
        with(holder.binding) {
            Glide
                .with(root.context)
                .load(human?.posterUrl)
                .placeholder(R.drawable.outline)
                .into(actorAvatar)
             actorName.text = human?.nameRu
            actorHeroName.text = human?.nameEn
        }

        holder.itemView.setOnClickListener {
            human?.let { onClick(it) }
        }
    }


    class HumanViewHolder(val binding: ItemDetailActorRecyclerElementBinding) : RecyclerView.ViewHolder(binding.root)
    class DiffUtilCallback : DiffUtil.ItemCallback<HumanItem>() {
        override fun areItemsTheSame(oldItem: HumanItem, newItem: HumanItem): Boolean =
            oldItem.staffId == newItem.staffId

        override fun areContentsTheSame(oldItem: HumanItem, newItem: HumanItem): Boolean =
            oldItem == newItem
    }
}

