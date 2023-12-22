package com.app.skillcinema.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.skillcinema.R
import com.app.skillcinema.data.retrofit.Photo
import com.app.skillcinema.databinding.ItemReciclerPhotoBinding
import com.bumptech.glide.Glide

class PhotoAdapter(private val listPhoto: List<Photo>) : RecyclerView.Adapter<PhotoAdapter.PhotoVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoVH {
        val binding = ItemReciclerPhotoBinding.inflate(LayoutInflater.from(parent.context))
        return PhotoVH(binding)
    }

    override fun getItemCount()=listPhoto.size

    override fun onBindViewHolder(holder: PhotoVH, position: Int) {
        val item = listPhoto[position]
        Glide
            .with(holder.binding.root.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.outline)
            .into(holder.binding.photo)
    }


    class PhotoVH(val binding: ItemReciclerPhotoBinding) : RecyclerView.ViewHolder(binding.root)
}