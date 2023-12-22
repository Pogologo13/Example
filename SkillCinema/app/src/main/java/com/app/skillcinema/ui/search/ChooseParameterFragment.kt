package com.app.skillcinema.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.skillcinema.R
import com.app.skillcinema.databinding.FragmentChooseParameterBinding


class ChooseParameterFragment : Fragment() {

    private var countriesKey: Int? = null
    private var genresKey: Int? = null
    private var countriesValue: String? = null
    private var genresValue: String? = null
    private var orderKey: String? = null
    private var typeKey: String? = null
    private var ratingFromKey: Int? = null
    private var ratingToKey: Int? = null
    private var yearFromKey: Int? = null
    private var yearToKey: Int? = null
    private var keywordsKey: String? = null
    private var beenViewed: Boolean = false
    private var contentType: String? = null

    private var _binding: FragmentChooseParameterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            countriesKey = it.getInt(SearchParameterFragment.COUNTRIES_KEY)
            genresKey = it.getInt(SearchParameterFragment.GENRES_KEY)
            countriesValue = it.getString(SearchParameterFragment.COUNTRIES_VALUE)
            genresValue = it.getString(SearchParameterFragment.GENRES_VALUE)
            orderKey = it.getString(SearchParameterFragment.ORDER)
            typeKey = it.getString(SearchParameterFragment.TYPE)
            ratingFromKey = it.getInt(SearchParameterFragment.RATING_FROM)
            ratingToKey = it.getInt(SearchParameterFragment.RATING_TO)
            yearFromKey = it.getInt(SearchParameterFragment.YEAR_FROM)
            yearToKey = it.getInt(SearchParameterFragment.YEAR_TO)
            keywordsKey = it.getString(SearchParameterFragment.KEYWORDS)
            beenViewed = it.getBoolean(SearchParameterFragment.BEEN_VIEWED)
            contentType = it.getString(CONTENT_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseParameterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            countriesKey?.let { putInt(SearchFragment.COUNTRIES_KEY, it) }
            genresKey?.let { putInt(SearchFragment.GENRES_KEY, it) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.mainTitle.text = contentType
        if (contentType == TYPE_IS_COUNTRY) {
            val countryAdapter =
                ChooseParameterAdapter({ index -> onClickItem(index) }, COUNTRIES_MAP, countriesKey)
            binding.recycler.adapter = countryAdapter
        } else {
            val genreAdapter =
                ChooseParameterAdapter({ index -> onClickItem(index) }, GENRES_MAP, genresKey)
            binding.recycler.adapter = genreAdapter
        }
    }

    private fun onClickItem(index: Int) {

        if (contentType == TYPE_IS_COUNTRY) {
            val value = COUNTRIES_MAP[index].toString()
            countriesKey = index
            countriesValue = value
        } else {
            val value = GENRES_MAP[index].toString()
            genresKey = index
            genresValue = value
        }

        val bundle = Bundle().apply {
            instance(this)
        }
        findNavController().navigate(R.id.searchParameterFragment, bundle)
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
        putInt(YEAR_FROM, yearFromKey ?: 1000)
        putInt(YEAR_TO, yearToKey ?: 3000)
        putString(KEYWORDS, keywordsKey)
        putBoolean(BEEN_VIEWED, beenViewed)
    }

    private companion object {
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
        const val TYPE_IS_COUNTRY = "Cтрана"
        const val CONTENT_TYPE = "content_type"

        val COUNTRIES_MAP = mapOf(
            0 to "Все страны",
            1 to "США",
            2 to "Швейцария",
            3 to "Франция",
            4 to "Польша",
            5 to "Великобритания",
            6 to "Швеция",
            9 to "Германия",
            10 to "Италия",
            14 to "Канада",
            16 to "Япония",
            33 to "СССР",
            34 to "Россия",
            49 to "Корея Южная"
        )

        val GENRES_MAP = mapOf(
            0 to "Все жанры",
            1 to "Триллер",
            2 to "Драма",
            3 to "Криминал",
            4 to "Мелодрама",
            5 to "Детектив",
            6 to "Фантастика",
            7 to "Приключения",
            8 to "Биография",
            9 to "Фильм-нуар",
            10 to "Вестерн",
            11 to "Боевик",
            12 to "Фэнтези",
            13 to "Комедия",
            14 to "Военный",
            15 to "История",
            16 to "Музыка",
            17 to "Ужасы",
            18 to "Мультфильм",
            19 to "Семейный",
            20 to "Мюзикл"
        )
    }
}
























