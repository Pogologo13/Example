package com.example.userdata.presenters.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.userdata.databinding.LoadStateBinding

class MyLoadStateAdapter: LoadStateAdapter<LoadStateViewHolder>() {

    //при загрузке данные не изменяются
    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) = Unit

    //инстенс дщфв стейт
    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
       val binding = LoadStateBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LoadStateViewHolder(binding)
    }
}

class LoadStateViewHolder(binding: LoadStateBinding): ViewHolder(binding.root)