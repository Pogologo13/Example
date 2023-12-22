package com.app.skillcinema.ui.detail.photo_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.skillcinema.R
import com.app.skillcinema.data.retrofit.Photo
import com.app.skillcinema.databinding.ItemReciclerPhotoBinding
import com.bumptech.glide.Glide

class PhotoPagedAdapter(private val onClick: (Int) -> Unit) :
    PagingDataAdapter<Photo, PhotoPagedAdapter.PhotoVH>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoVH {
        val binding = ItemReciclerPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoVH(binding)
    }

    override fun getItemCount(): Int {
        return if (super.getItemCount() > 20) 20 else super.getItemCount()
    }

    override fun onBindViewHolder(holder: PhotoVH, position: Int) {
        val photo = getItem(position)

        if (position < 20) {
            Glide
                .with(holder.binding.root.context)
                .load(photo?.imageUrl)
                .placeholder(R.drawable.outline)
                .into(holder.binding.photo)

            holder.binding.photo.setOnClickListener {
                onClick(position)
            }
        }
    }


    class PhotoVH(val binding: ItemReciclerPhotoBinding) : RecyclerView.ViewHolder(binding.root)
    class DiffUtilCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
            oldItem.imageUrl == newItem.imageUrl

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
            oldItem == newItem
    }
}
