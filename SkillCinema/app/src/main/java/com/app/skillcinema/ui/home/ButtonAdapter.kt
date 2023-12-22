package com.app.skillcinema.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.skillcinema.databinding.ButtonRecyclerItemViewBinding

class ButtonAdapter(private val onClickButton: (String)-> Unit, private val type: String) : RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder>() {

    class ButtonViewHolder( val binding: ButtonRecyclerItemViewBinding) : ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val binding = ButtonRecyclerItemViewBinding.inflate(LayoutInflater.from(parent.context))
        return ButtonViewHolder(binding)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        holder.binding.buttonMore.setOnClickListener {
            onClickButton(type)
        }
    }
}
