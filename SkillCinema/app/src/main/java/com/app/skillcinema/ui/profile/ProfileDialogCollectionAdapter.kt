package com.app.skillcinema.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.skillcinema.R
import com.app.skillcinema.data.room.SavedListWithItem
import com.app.skillcinema.databinding.ItemRecyclerProfileDialogAdapterBinding

class ProfileDialogCollectionAdapter(
    private val onClickDelete: (String) -> Unit,
    private val onClickShowAll: (SavedListWithItem) -> Unit,
    private val savedListItem: List<SavedListWithItem>
) :
    RecyclerView.Adapter<ProfileCollectionVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileCollectionVH {
        val binding = ItemRecyclerProfileDialogAdapterBinding.inflate(LayoutInflater.from(parent.context))
        return ProfileCollectionVH(binding)
    }

    override fun getItemCount() = savedListItem.size
    override fun onBindViewHolder(holder: ProfileCollectionVH, position: Int) {
        val item = savedListItem[position]

        holder.binding.icon.background = when (position) {
            0 -> {
                holder.binding.dismissButton.visibility = View.GONE
                ResourcesCompat.getDrawable(holder.binding.root.resources, R.drawable.ic_like_cheked, null)
            }

            1 -> {
                holder.binding.dismissButton.visibility = View.GONE
                ResourcesCompat.getDrawable(holder.binding.root.resources, R.drawable.ic_mark_checked, null)
            }

            else -> ResourcesCompat.getDrawable(holder.binding.root.resources, R.drawable.ic_personally, null)
        }

        holder.binding.listName.text = item.savedList.listItemName
        holder.binding.dieButton.text = item.savedItem?.count().toString()

        holder.binding.dismissButton.setOnClickListener {
            onClickDelete(item.savedList.listItemName)
        }

        holder.binding.dieButton.setOnClickListener {
            onClickShowAll(item)
        }
    }
}

class ProfileCollectionVH(val binding: ItemRecyclerProfileDialogAdapterBinding) : RecyclerView.ViewHolder(binding.root)