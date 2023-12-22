package com.app.skillcinema.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.skillcinema.databinding.ItemDialogRecyclerAddCollectionBinding

private const val ONE = 1
class AddCollectionAdapter(private val onClickAdd: (Unit) -> Unit) :
    RecyclerView.Adapter<AddCollectionVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddCollectionVH {
        val binding = ItemDialogRecyclerAddCollectionBinding.inflate(LayoutInflater.from(parent.context))
        return AddCollectionVH(binding)
    }

    override fun getItemCount() = ONE

    override fun onBindViewHolder(holder: AddCollectionVH, position: Int) {
        holder.binding.root.setOnClickListener {
            onClickAdd(Unit)
        }
    }
}

class AddCollectionVH(val binding: ItemDialogRecyclerAddCollectionBinding) : RecyclerView.ViewHolder(binding.root)