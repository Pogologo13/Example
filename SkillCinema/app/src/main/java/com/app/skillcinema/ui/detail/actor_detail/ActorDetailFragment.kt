package com.app.skillcinema.ui.detail.actor_detail

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
import com.app.skillcinema.databinding.FragmentActorDetailBinding
import com.app.skillcinema.ui.detail.DetailViewModel
import com.app.skillcinema.ui.detail.DetailViewModelFactory
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val HUMAN_ID = "stuff_id"
private const val KINOPOISK_ID = "kinopoisk_id"
const val DATE = "date"

@AndroidEntryPoint
class ActorDetailFragment : Fragment() {
    private var stuffId: Int? = null
    private var kinopoiskId: String? = null
    private var date: String? = null
    private var _binding: FragmentActorDetailBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mainViewModelFactory: DetailViewModelFactory

    private val viewModel: DetailViewModel by viewModels { mainViewModelFactory }
    private val filmsWithActorList = emptyList<FilmWithActor>().toMutableList()

    //    private val sortedList = emptyList<FilmItem>().toMutableList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            stuffId = it.getInt(HUMAN_ID)
            kinopoiskId = it.getString(KINOPOISK_ID)
            date = it.getString(DATE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getHumanDetail(stuffId!!).collectLatest { humanDetail ->
                        if (humanDetail != null) {
                            Glide
                                .with(requireContext())
                                .load(humanDetail.posterUrl)
                                .placeholder(R.drawable.outline)
                                .into(binding.avatar)

                            binding.profession.text = humanDetail.profession
                            binding.nameOfActor.text = humanDetail.nameRu ?: humanDetail.nameEn
                            val filmsQuantity = humanDetail.films?.size?.let {
                                resources.getQuantityString(R.plurals.films, it, it)
                            }
                            binding.filmCount.text = filmsQuantity

                            humanDetail.films?.forEach { filmWithActor ->
                                filmsWithActorList.add(filmWithActor)
                            }
                            val sortedList = filmsWithActorList.apply {
                                sortWith(compareBy { it.rating })
                            }.distinctBy { it.filmId }
                            val size = 20
                            if (sortedList.size >= size)
                                viewModel.setFilmsWithActorList(sortedList.takeLast(size))

                            val bundle = Bundle().apply {
                                putInt(HUMAN_ID, humanDetail.personId)
                                putString(DATE,date)
                            }

                            binding.theMoreBestFilms.setOnClickListener {
                                findNavController().navigate(R.id.allSerialsFragment, bundle)
                            }

                            binding.toAllFilms.setOnClickListener {
                                findNavController().navigate(R.id.allFilmsWithActor, bundle)
                            }
                            binding.loadStateIndicator.visibility = View.VISIBLE
                        }
                        launch {
                            viewModel.filmWithActor.collect {
                                if (it != emptyList<FilmItem>()) {
                                    binding.loadStateIndicator.visibility = View.GONE
                                    val adapter =
                                        FilmWithActorDetailAdapterWithBottomText({ film -> onClickFilm(film) }, it.reversed())
                                    binding.recyclerOfTheBest.adapter = adapter
                                }
                            }

                        }
                    }
                }
            }
        }
    }


    private fun onClickFilm(film: FilmItem) {
        val bundle = Bundle().apply {
            putInt(KINOPOISK_ID, film.kinopoiskId)
        }
        viewModel.setFilm(film.kinopoiskId, date, film.premiereRu)
        findNavController().navigate(R.id.detailFragment, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}