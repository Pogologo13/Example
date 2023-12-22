package com.app.skillcinema.ui.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.skillcinema.R
import com.app.skillcinema.databinding.ItemRecyclerChooseParameterAdapterBinding

class ChooseParameterAdapter(
    private val onClick: (Int) -> Unit,
    private val contentMap: Map<Int, String>,
    private val itemKey: Int?
) :
    RecyclerView.Adapter<ChooseParameterAdapter.ChooseParamVH>() {

    class ChooseParamVH(val binding: ItemRecyclerChooseParameterAdapterBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseParamVH {
        val binding = ItemRecyclerChooseParameterAdapterBinding.inflate(LayoutInflater.from(parent.context))
        return ChooseParamVH(binding)
    }

    override fun getItemCount() = contentMap.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ChooseParamVH, position: Int) {
        val indexList = emptyList<Int>().toMutableList()
        val itemList = emptyList<String>().toMutableList()

        contentMap.map { (k, v) ->
            indexList.add(k)
            itemList.add(v)
        }

        val item = itemList[position]
        val indexOfMap = contentMap.entries.find { it.value == item }?.key ?: 0

        if(indexOfMap == itemKey) holder.binding.root.setBackgroundColor(R.color.gray_for_background)
        holder.binding.title.text = item

        holder.binding.root.setOnClickListener {
            onClick(indexList[position])
        }

    }
}