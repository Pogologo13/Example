package com.example.userdata.presenters.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.userdata.data.User
import com.example.userdata.databinding.CustomElementViewBinding

class MyPagedAdapter(
    private val onClick: (User, ImageView) -> Unit
) : PagingDataAdapter<User, CustomViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding =
            CustomElementViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = getItem(position)
        val login = "Login: ${item?.login}"
        val id = "Id: ${item?.id}"
        with(holder.binding) {
            loginText.text = login
            idText.text = id
            Glide
                .with(root.context)
                .load(item?.avatar_url)
                .into(photo)
        }

        holder.binding.root.setOnClickListener {

            //передаем текущий элемент и вью (для анимации)
            val imageView =  holder.binding.photo
           imageView.transitionName = "transition_name"

            item?.let { item -> onClick(item, imageView) }
        }

    }
}

class CustomViewHolder(val binding: CustomElementViewBinding) : ViewHolder(binding.root)

class DiffUtilCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
}