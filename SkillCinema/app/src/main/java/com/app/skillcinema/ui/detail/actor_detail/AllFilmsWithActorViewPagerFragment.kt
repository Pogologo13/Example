package com.app.skillcinema.ui.detail.actor_detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.app.skillcinema.R
import com.app.skillcinema.data.retrofit.FilmItem
import com.app.skillcinema.data.retrofit.FilmWithActor
import com.app.skillcinema.databinding.FragmentViewpagerEpisodesLayoutBinding
import com.app.skillcinema.ui.detail.DetailViewModel
import com.app.skillcinema.ui.detail.DetailViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AllFilmsWithActorViewPagerFragment : Fragment() {
    private var _binding: FragmentViewpagerEpisodesLayoutBinding? = null
    private val binding get() = _binding!!

    private var filmList: List<FilmWithActor>? = null
    private var date: String? = null

    @Inject
    lateinit var mainViewModelFactory: DetailViewModelFactory
    private val viewModel: DetailViewModel by viewModels { mainViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewpagerEpisodesLayoutBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.apply {
            filmList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getSerializable(LIST_FILM_WITH_ACTOR, Array<FilmWithActor>::class.java)?.toList()
            } else @Suppress("DEPRECATION") (getSerializable(LIST_FILM_WITH_ACTOR) as Array<FilmWithActor>).toList()
            date = this.getString(DATE)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (filmList != null) {
                    if (filmList?.size!! >= SIZE)
                        viewModel.setFilmsWithActorList(filmList!!.take(SIZE))
                    else viewModel.setFilmsWithActorList(filmList!!.toList())
                }
                viewModel.filmWithActor.take(2).collect {
                    if (it != emptyList<FilmItem>()) {
                        val adapter =
                            FilmActorDetailAdapter({ film -> onClickFilm(film) }, it)
                        binding.recycler.adapter = adapter
                        binding.loadIndicator.visibility = View.GONE
                    }
                }
            }

        }
    }

    private fun onClickFilm(film: FilmItem) {
        val bundle = Bundle().apply {
            putSerializable(KINOPOISK_ID, film)
        }
        viewModel.setFilm(film.kinopoiskId, date, film.premiereRu)
        findNavController().navigate(R.id.detailFragment, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private companion object {
        const val LIST_FILM_WITH_ACTOR = "films_with_actor"
        const val KINOPOISK_ID = "kinopoisk_id"
        const val SIZE = 20
        const val DATE = "date"
    }
}