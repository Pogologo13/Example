package com.app.skillcinema.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.skillcinema.R
import com.app.skillcinema.data.retrofit.FilmItem
import com.app.skillcinema.data.retrofit.Genre
import com.app.skillcinema.databinding.ViewElementRecycleBinding
import com.bumptech.glide.Glide

class SimilarAdapter (private val onClickSimilar: (FilmItem) -> Unit, private val similarList: List<FilmItem?>) :
    RecyclerView.Adapter<SimilarAdapter.SimilarVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarVH {
        val binding = ViewElementRecycleBinding.inflate(LayoutInflater.from(parent.context))
        return SimilarVH(binding)
    }

    override fun getItemCount() = similarList.size

    override fun onBindViewHolder(holder: SimilarVH, position: Int) {
        val film = similarList[position]
        with(holder.binding) {
            if(film?.viewed == true){
                posterPreview.foreground = ResourcesCompat.getDrawable(holder.binding.root.context.resources, R.drawable.foreground_viewed, null)
                eye.visibility = View.VISIBLE
            }
            Glide
                .with(root.context)
                .load(film?.posterUrlPreview)
                .placeholder(R.drawable.outline)
                .into(posterPreview)
            filmTitle.text = film?.nameRu
            filmDescription.text = film?.genres?.let { getGenres(it) }
            val ratio = film?.ratingKinopoisk ?: 0.0
            dieTitle.text = ratio.toString()
        }

        holder.itemView.setOnClickListener {
            film?.let { film -> onClickSimilar(film) }
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

    class SimilarVH(val binding: ViewElementRecycleBinding): ViewHolder(binding.root)
}
