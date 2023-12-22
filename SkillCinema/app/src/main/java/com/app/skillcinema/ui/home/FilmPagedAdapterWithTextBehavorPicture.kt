package com.app.skillcinema.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.skillcinema.R
import com.app.skillcinema.data.retrofit.FilmItem
import com.app.skillcinema.data.retrofit.Genre
import com.app.skillcinema.databinding.ViewElementRecycleBinding
import com.bumptech.glide.Glide


class FilmPagedAdapterWithTextBehavorPicture(
    private val onClick: (FilmItem) -> Unit
) : PagingDataAdapter<FilmItem, FilmPagedAdapterWithTextBehavorPicture.FilmViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val binding = ViewElementRecycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(binding)
    }

    @SuppressLint("PrivateResource")
    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = getItem(position)
        with(holder.binding) {
            if (film?.viewed == true) {
                posterPreview.foreground = ResourcesCompat.getDrawable(
                    holder.binding.root.context.resources,
                    R.drawable.foreground_viewed,
                    null
                )
                eye.visibility = View.VISIBLE
            } else {
                posterPreview.foreground = ResourcesCompat.getDrawable(
                    holder.binding.root.context.resources,
                    com.google.android.material.R.drawable.m3_tabs_transparent_background,
                    null
                )
                eye.visibility = View.GONE
            }
            Glide
                .with(root.context)
                .load(film?.posterUrlPreview)
                .placeholder(R.drawable.outline)
                .into(posterPreview)
            filmTitle.text = film?.nameRu ?: film?.nameEn
            filmDescription.text = film?.genres?.let { getGenres(it) }
            val ratio = film?.ratingKinopoisk ?: 0.0
            dieTitle.text = ratio.toString()
        }

        holder.itemView.setOnClickListener {
            film?.let { filmItem -> onClick(filmItem) }
        }
    }

    private fun getGenres(film: List<Genre>): StringBuilder {
        val stringGenre = StringBuilder()
        var x = 0
        film.forEach { genre ->
            stringGenre.append(genre.genre)
            if (x < film.size - 1) {
                stringGenre.append(", ")
                ++x
            }
        }
        return stringGenre
    }

    class FilmViewHolder(val binding: ViewElementRecycleBinding) : ViewHolder(binding.root)

    class DiffUtilCallback : DiffUtil.ItemCallback<FilmItem>() {
        override fun areItemsTheSame(oldItem: FilmItem, newItem: FilmItem): Boolean =
            oldItem.kinopoiskId == newItem.kinopoiskId

        override fun areContentsTheSame(oldItem: FilmItem, newItem: FilmItem): Boolean =
            oldItem == newItem
    }
}