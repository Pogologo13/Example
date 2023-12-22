package com.app.skillcinema.ui.detail.seasons_detail

import android.annotation.SuppressLint
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
import com.app.skillcinema.databinding.FragmentAllSerialsBinding
import com.app.skillcinema.ui.detail.DetailViewModel
import com.app.skillcinema.ui.detail.DetailViewModelFactory
import com.app.skillcinema.ui.detail.actor_detail.FilmWithActorDetailAdapterWithBottomText
import com.app.skillcinema.ui.home.FilmPagedAdapterWithTextBehavorPicture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AllSerialsFragment : Fragment() {
    private var _binding: FragmentAllSerialsBinding? = null
    private val binding get() = _binding!!
    private var type: String? = null
    private var date: String? = null
    private var stuffId: Int? = null
    private var savedListName: String? = null

    @Inject
    lateinit var mainViewModelFactory: DetailViewModelFactory

    private val viewModel: DetailViewModel by viewModels { mainViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(TYPE_OF_COLLECTION)
            stuffId = it.getInt(STUFF_ID)
            savedListName = it.getString(SAVED_LIST_NAME)
            date = it.getString(DATE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllSerialsBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                //Подборки по теме
                type?.let {
                    launch {
                        binding.title.text = when (type) {
                            SERIALS -> RU_SERIALS
                            CATASTROPHE_THEME -> RU_CATASTROPHE_THEME
                            TOP_POPULAR_ALL -> RU_TOP_POPULAR_ALL
                            TOP_POPULAR_MOVIES -> RU_TOP_POPULAR_MOVIES
                            TOP_250_MOVIES -> RU_TOP_250_MOVIES
                            CLOSES_RELEASES -> RU_CLOSES_RELEASES
                            LOVE_THEME -> RU_LOVE_THEME
                            FAMILY -> RU_FAMILY
                            else -> RU_KIDS_ANIMATION_THEME
                        }

                        binding.loadStateIndicator.visibility = View.GONE
                        val pagedAdapter =
                            FilmPagedAdapterWithTextBehavorPicture { filmCollection -> onClick(filmCollection) }
                        binding.recyclerAllSerials.adapter = pagedAdapter
                        viewModel.getFilmCollections(it, GET_ALL_SERIALS).collect { filmItem ->
                            pagedAdapter.submitData(filmItem)
                        }
                    }
                }

                //Премьеры фильмов
                date?.let {
                    viewModel.setPremiere(it).collect { filmItem ->
                        binding.title.text = "Премьеры"
                        binding.loadStateIndicator.visibility = View.GONE
                        val premierAdapter =
                            FilmWithActorDetailAdapterWithBottomText(
                                { filmCollection -> onClick(filmCollection) },
                                filmItem
                            )
                        binding.recyclerAllSerials.adapter = premierAdapter
                    }
                }

                //Фильмы с актером
                stuffId?.let { stuff ->
                    if (stuff > 0) {
                        launch {
                            viewModel.getHumanDetail(stuff).collect { humanDetail ->
                                binding.title.text = "Фильмы c ${humanDetail?.nameRu ?: humanDetail?.nameEn}"
                                val filmsWithActorList = emptyList<FilmWithActor>().toMutableList()
                                if (humanDetail != null) {
                                    humanDetail.films?.forEach { filmWithActor ->
                                        filmsWithActorList.add(filmWithActor)
                                    }

                                    val sortedList = filmsWithActorList.apply {
                                        sortWith(compareBy { it.rating })
                                    }.distinctBy { it.filmId }
                                    val size = 30
                                    if (sortedList.size >= size)
                                        viewModel.setFilmsWithActorList(sortedList.takeLast(size))
                                }
                            }
                        }

                        launch {
                            viewModel.filmWithActor.collect {
                                if (it != emptyList<FilmItem>()) {
                                    binding.loadStateIndicator.visibility = View.GONE
                                    val adapter =
                                        FilmWithActorDetailAdapterWithBottomText(
                                            { film -> onClick(film) },
                                            it.reversed()
                                        )
                                    binding.recyclerAllSerials.adapter = adapter
                                }
                            }
                        }
                    }
                }

                //Фильмы из отложенного списка
                savedListName?.let {
                    binding.title.text = savedListName
                    launch {
                        viewModel.getListByName(savedListName!!)?.collect { listSavedItem ->
                            val listFilms = emptyList<FilmItem>().toMutableList()
                            listSavedItem.onEach { listFilms.add(it.listFilms) }
                            binding.loadStateIndicator.visibility = View.GONE
                            val adapter = FilmWithActorDetailAdapterWithBottomText({ film -> onClick(film) }, listFilms)
                            binding.recyclerAllSerials.adapter = adapter
                        }
                    }
                }


            }
        }
    }

    private fun onClick(filmItem: FilmItem) {
        val bundle = Bundle().apply {
            putSerializable(KINOPOISK_ID, filmItem)
        }
        findNavController().navigate(R.id.detailFragment, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private companion object {
        const val GET_ALL_SERIALS = true
        const val KINOPOISK_ID = "kinopoisk_id"
        const val TYPE_OF_COLLECTION = "type"
        const val STUFF_ID = "stuff_id"
        const val SAVED_LIST_NAME = "saved_list_name"
        const val DATE = "date"

        const val SERIALS = "SERIALS"
        const val CATASTROPHE_THEME = "CATASTROPHE_THEME"
        const val TOP_POPULAR_ALL = "TOP_POPULAR_ALL"
        const val TOP_POPULAR_MOVIES = "TOP_POPULAR_MOVIES"
        const val TOP_250_MOVIES = "TOP_250_MOVIES"
        const val CLOSES_RELEASES = "CLOSES_RELEASES"
        const val LOVE_THEME = "LOVE_THEME"
        const val FAMILY = "FAMILY"

        const val RU_SERIALS = "Сериалы"
        const val RU_CATASTROPHE_THEME = "Катастрофы"
        const val RU_TOP_POPULAR_ALL = "Популярные фильмы и сериалы"
        const val RU_TOP_POPULAR_MOVIES = "Смотрят сейчас"
        const val RU_TOP_250_MOVIES = "Топ 250"
        const val RU_CLOSES_RELEASES = "Смотрели раньше"
        const val RU_LOVE_THEME = "Романтичесике"
        const val RU_FAMILY = "Семейные"
        const val RU_KIDS_ANIMATION_THEME = "Мультики"
    }
}