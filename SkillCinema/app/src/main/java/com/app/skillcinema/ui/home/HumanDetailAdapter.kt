package com.app.skillcinema.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.skillcinema.R
import com.app.skillcinema.data.retrofit.HumanDetailDto
import com.app.skillcinema.databinding.ViewElementRecycleBinding
import com.bumptech.glide.Glide

class HumanDetailAdapter(private val onClickActor: (HumanDetailDto) -> Unit, private val humanList: List<HumanDetailDto>) :
    RecyclerView.Adapter<HumanDetailAdapter.ActorVH>() {

    class ActorVH(val binding: ViewElementRecycleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorVH {
        val binding = ViewElementRecycleBinding.inflate(LayoutInflater.from(parent.context))
        return ActorVH(binding)
    }

    override fun getItemCount(): Int = humanList.size

    override fun onBindViewHolder(holder: ActorVH, position: Int) {
        val human = humanList[position]
        with(holder.binding) {
            Glide
                .with(root.context)
                .load(human.posterUrl)
                .placeholder(R.drawable.outline)
                .into(posterPreview)
            filmTitle.text = human.nameRu ?: human.nameEn
            filmDescription.text = human.profession
            die.visibility = View.GONE
            dieTitle.visibility = View.GONE

            root.setOnClickListener {
                onClickActor(human)
            }
        }
    }
}