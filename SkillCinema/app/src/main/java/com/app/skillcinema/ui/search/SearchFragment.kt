package com.app.skillcinema.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.app.skillcinema.R
import com.app.skillcinema.data.retrofit.FilmItem
import com.app.skillcinema.data.retrofit.HumanItem
import com.app.skillcinema.databinding.FragmentSearchBinding
import com.app.skillcinema.ui.home.HumanPagedAdapter
import com.app.skillcinema.utils.BottomBarHandle
import com.app.skillcinema.utils.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mainViewModelFactory: SearchViewModelFactory

    private val viewModel: SearchViewModel by viewModels { mainViewModelFactory }
    private lateinit var filmAdapter: FilmPagedAdapterWithTextRightOfPicture
    private lateinit var humanAdapter: HumanPagedAdapter
    private var filmScope = lifecycleScope
    private var humanScope = lifecycleScope


    private var countriesKey: Int? = null
    private var genresKey: Int? = null
    private var countriesValue: String = "Все страны"
    private var genresValue: String = "Все жанры"
    private var orderKey: String = "RATING"
    private var typeKey: String = "ALL"
    private var ratingFromKey: Int? = null
    private var ratingToKey: Int? = null
    private var yearFromKey: Int? = null
    private var yearToKey: Int? = null
    private var keywordsKey: String = ""
    private var beenViewed: Boolean = false
    var year by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            countriesKey = it.getInt(COUNTRIES_KEY)
            genresKey = it.getInt(GENRES_KEY)
            countriesValue = it.getString(COUNTRIES_VALUE) ?: countriesValue
            genresValue = it.getString(GENRES_VALUE) ?: genresValue
            orderKey = it.getString(ORDER) ?: orderKey
            typeKey = it.getString(TYPE) ?: typeKey
            ratingFromKey = it.getInt(RATING_FROM)
            ratingToKey = it.getInt(RATING_TO)
            yearFromKey = it.getInt(YEAR_FROM)
            yearToKey = it.getInt(YEAR_TO)
            keywordsKey = it.getString(KEYWORDS) ?: keywordsKey
            beenViewed = it.getBoolean(BEEN_VIEWED)
        }
        arguments?.let {
            countriesKey = it.getInt(COUNTRIES_KEY)
            genresKey = it.getInt(GENRES_KEY)
            countriesValue = it.getString(COUNTRIES_VALUE) ?: countriesValue
            genresValue = it.getString(GENRES_VALUE) ?: genresValue
            orderKey = it.getString(ORDER) ?: orderKey
            typeKey = it.getString(TYPE) ?: typeKey
            ratingFromKey = it.getInt(RATING_FROM)
            ratingToKey = it.getInt(RATING_TO)
            yearFromKey = it.getInt(YEAR_FROM)
            yearToKey = it.getInt(YEAR_TO)
            keywordsKey = it.getString(KEYWORDS) ?: keywordsKey
            beenViewed = it.getBoolean(BEEN_VIEWED)
        }
        if (countriesKey == 0) countriesKey = null
        if (genresKey == 0) genresKey = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filmAdapter = FilmPagedAdapterWithTextRightOfPicture { filmCollection -> onClickFilm(filmCollection) }
        humanAdapter = HumanPagedAdapter { human -> onClickHuman(human) }
        val concatAdapter = ConcatAdapter(filmAdapter, humanAdapter)

        binding.recycler.adapter = concatAdapter

        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)

        binding.searchText.addTextChangedListener {
            binding.recycler.layoutManager?.scrollToPosition(0)
            keywordsKey = it.toString()
            viewModel.findingFilms(
                countriesKey,
                genresKey,
                orderKey,
                typeKey,
                ratingFromKey ?: 0,
                ratingToKey ?: 10,
                yearFromKey ?: (year - 20),
                yearToKey ?: year,
                keywordsKey,
                beenViewed
            )
        }

        humanAdapter.addLoadStateListener {
            if (it.append.endOfPaginationReached) {
                noFoundedItem()
            }
            if (it.prepend.endOfPaginationReached) {
                noFoundedItem()
            }
        }

        filmAdapter.addLoadStateListener {
            if (it.prepend.endOfPaginationReached) {
                noFoundedItem()
            }
            if (it.append.endOfPaginationReached) {
                noFoundedItem()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect { state ->

                    when (state) {
                        is State.Error -> binding.textFieldLayout.error = state.message
                        is State.Loading -> {
                            binding.textFieldLayout.error = null
                            binding.indicator.visibility = View.VISIBLE
                        }

                        is State.Success -> {
                            binding.indicator.visibility = View.GONE
                            binding.textFieldLayout.error = null
                            commit()
                            noFoundedItem()
                        }
                    }

                }
            }
        }

        binding.parameterButton.setOnClickListener {
            val bundle = instance(Bundle())
            findNavController().navigate(R.id.searchParameterFragment, bundle)
        }
    }

    private fun commit() {
        filmScope.launch {
            viewModel.films.collect { films ->
                if (films != null) {
                    filmAdapter.submitData(films)
                } else {
                    noFoundedItem()
                    this.coroutineContext.cancelChildren()
                }
            }
        }

        humanScope.launch {
            viewModel.humans.collect { humans ->
                if (humans != null) {
                    humanAdapter.submitData(humans)
                } else {
                    noFoundedItem()
                    this.coroutineContext.cancelChildren()
                }
            }
        }
    }

    private fun noFoundedItem() {
        try {
            val count = binding.recycler.childCount
            binding.test.text = if (count < 0)
                "По вашему запросу совпадений не найдено" else ""
        } catch (e: Exception) {
            return
        }
    }


    private fun onClickHuman(human: HumanItem) {
        val bundle = Bundle().apply {
            human.kinopoiskId?.let { putInt(STUFF_ID, it) }
        }
        human.kinopoiskId?.let { viewModel.setHumanDetail(it) }
        findNavController().navigate(R.id.actorDetailFragment, bundle)
    }

    private fun onClickFilm(filmItem: FilmItem) {
        val bundle = Bundle().apply {
            putInt(KINOPOISK_ID, filmItem.kinopoiskId)
        }
        findNavController().navigate(R.id.detailFragment, bundle)
    }

    override fun onResume() {
        super.onResume()
        val bottomHandle = (activity as BottomBarHandle)
        bottomHandle.showBottomBar()
        viewModel.findingFilms(
            countriesKey,
            genresKey,
            orderKey,
            typeKey,
            ratingFromKey ?: 0,
            ratingToKey ?: 10,
            yearFromKey ?: (year - 20),
            yearToKey ?: year,
            keywordsKey,
            beenViewed
        )



        if (keywordsKey != "") binding.searchText.setText(keywordsKey)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instance(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        val bottomHandle = (activity as BottomBarHandle)
        val bundle = instance(Bundle())
        bottomHandle.getArguments(bundle)
    }

    fun instance(bundle: Bundle) = bundle.apply {
        countriesKey?.let { countryIndex -> putInt(COUNTRIES_KEY, countryIndex) }
        genresKey?.let { genreIndex -> putInt(GENRES_KEY, genreIndex) }
        putString(COUNTRIES_VALUE, countriesValue)
        putString(GENRES_VALUE, genresValue)
        putString(ORDER, orderKey)
        putString(TYPE, typeKey)
        putInt(RATING_TO, ratingToKey ?: 10)
        putInt(RATING_FROM, ratingFromKey ?: 0)
        putInt(YEAR_FROM, yearFromKey ?: (year - 20))
        putInt(YEAR_TO, yearToKey ?: year)
        putString(KEYWORDS, keywordsKey)
        putBoolean(BEEN_VIEWED, beenViewed)
    }

    companion object {
        private const val STUFF_ID = "stuff_id"
        const val KINOPOISK_ID = "kinopoisk_id"
        const val COUNTRIES_KEY = "countries_index"
        const val GENRES_KEY = "genres_index"
        const val COUNTRIES_VALUE = "countries_string"
        const val GENRES_VALUE = "genres_string"
        const val ORDER = "order"
        const val TYPE = "type"
        const val RATING_FROM = "ratingFrom"
        const val RATING_TO = "ratingTo"
        const val YEAR_FROM = "yearFrom"
        const val YEAR_TO = "yearTo"
        const val KEYWORDS = "keywords"
        const val BEEN_VIEWED = "viewed"


    }
}
