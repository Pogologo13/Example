package com.app.skillcinema.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.skillcinema.R
import com.app.skillcinema.data.retrofit.HumanItem
import com.app.skillcinema.databinding.ItemDetailActorRecyclerElementBinding
import com.bumptech.glide.Glide

class ActorAdapter(private val onClickActor: (HumanItem) -> Unit, private val humanList: List<HumanItem?>) :
    RecyclerView.Adapter<ActorAdapter.ActorViewHolder>() {

    class ActorViewHolder(val binding: ItemDetailActorRecyclerElementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val binding = ItemDetailActorRecyclerElementBinding.inflate(LayoutInflater.from(parent.context))
        return ActorViewHolder(binding)
    }

    override fun getItemCount(): Int = humanList.size

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        val human = humanList[position]
        with(holder.binding) {
            Glide
                .with(root.context)
                .load(human?.posterUrl)
                .placeholder(R.drawable.outline)
                .into(actorAvatar)
            actorName.text = if (human?.nameRu != null) human.nameRu else human?.nameEn
            actorHeroName.text =
                    human?.professionText?.replace("ы","")

            root.setOnClickListener {
                human?.let { human -> onClickActor(human) }
            }
        }
    }
}
