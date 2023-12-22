package com.app.skillcinema.ui.detail.seasons_detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.skillcinema.data.retrofit.Episode
import com.app.skillcinema.databinding.EpisodeDetailElementBinding

class EpisodeDetailRecyclerAdapter(private val episodesList: List<Episode>) : RecyclerView.Adapter<EpisodeDetailVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeDetailVH {
        val binding = EpisodeDetailElementBinding.inflate(LayoutInflater.from(parent.context))
        return EpisodeDetailVH(binding)
    }

    override fun getItemCount() = episodesList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EpisodeDetailVH, position: Int) {
        val item = episodesList[position]
        holder.binding.episodeNumber.text = "${item.episodeNumber}. "
        holder.binding.episodeDate.text = item.releaseDate
        holder.binding.episodeName.text = when {
            item.nameRu != null -> item.nameRu
            item.nameEn != null -> item.nameEn
            else -> "Название неизвестно"
        }
    }
}

class EpisodeDetailVH(val binding: EpisodeDetailElementBinding) : ViewHolder(binding.root)