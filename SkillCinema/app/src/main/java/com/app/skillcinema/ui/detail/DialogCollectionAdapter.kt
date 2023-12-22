package com.app.skillcinema.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.skillcinema.data.retrofit.FilmItem
import com.app.skillcinema.data.room.SavedListWithItem
import com.app.skillcinema.databinding.ItemDialogRecyclerNameOfCollectionBinding

class DialogCollectionAdapter(
    private val onClickAdd: (Boolean, String) -> Unit,
    private val savedListItem: List<SavedListWithItem>,
    private val _kinopoiskId: Int,
) :
    RecyclerView.Adapter<CollectionVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionVH {
        val binding = ItemDialogRecyclerNameOfCollectionBinding.inflate(LayoutInflater.from(parent.context))
        return CollectionVH(binding)
    }

    override fun getItemCount() = savedListItem.size
    override fun onBindViewHolder(holder: CollectionVH, position: Int) {
        val item = savedListItem[position]
        holder.binding.text.text = item.savedList.listItemName
        holder.binding.count.text = item.savedItem?.count().toString()

        val listForCheck = emptyList<FilmItem>().toMutableList()
        item.savedItem?.forEach { listForCheck.add(it.listFilms) }
        val checked = listForCheck.stream().anyMatch{ it.kinopoiskId == _kinopoiskId}
        holder.binding.checkbox.isChecked = checked

        holder.binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            onClickAdd(isChecked, item.savedList.listItemName)
        }
    }
}

class CollectionVH(val binding: ItemDialogRecyclerNameOfCollectionBinding) : RecyclerView.ViewHolder(binding.root)