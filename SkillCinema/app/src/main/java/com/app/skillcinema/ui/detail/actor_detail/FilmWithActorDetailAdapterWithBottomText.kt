package com.app.skillcinema.ui.detail.actor_detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.skillcinema.R
import com.app.skillcinema.data.retrofit.FilmItem
import com.app.skillcinema.data.retrofit.Genre
import com.app.skillcinema.databinding.ViewElementRecycleBinding
import com.bumptech.glide.Glide

class FilmWithActorDetailAdapterWithBottomText(private val onClickActor: (FilmItem) -> Unit, private val actorFilms: List<FilmItem>) :
    RecyclerView.Adapter<FilmWithActorDetailAdapterWithBottomText.FilmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val binding = ViewElementRecycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(binding)
    }

    override fun getItemCount()= actorFilms.size

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = actorFilms[position]
        if (film.viewed) {

            holder.binding.posterPreview.foreground =
                ResourcesCompat.getDrawable(holder.binding.root.context.resources, R.drawable.foreground_viewed, null)
            holder.binding.eye.visibility = View.VISIBLE
        }
        with(holder.binding) {
            Glide
                .with(root.context)
                .load(film.posterUrl)
                .placeholder(R.drawable.outline)
                .into(posterPreview)
            filmTitle.text = film.nameRu
            filmDescription.text = film.genres?.let { getGenres(it) }
            val ratio = film.ratingKinopoisk ?: 0.0
            dieTitle.text = ratio.toString()
        }
        holder.binding.root.setOnClickListener {
            onClickActor(film)
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

    class FilmViewHolder(val binding: ViewElementRecycleBinding) : RecyclerView.ViewHolder(binding.root)
}