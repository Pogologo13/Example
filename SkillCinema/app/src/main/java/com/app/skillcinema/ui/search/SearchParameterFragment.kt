package com.app.skillcinema.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.skillcinema.R
import com.app.skillcinema.databinding.FragmentSearchParameterBinding
import com.app.skillcinema.utils.BottomBarHandle
import java.util.*
import kotlin.properties.Delegates

class SearchParameterFragment : Fragment() {

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
    var year by Delegates.notNull<Int>()

    private var _binding: FragmentSearchParameterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            countriesKey = it.getInt(COUNTRIES_KEY)
            genresKey = it.getInt(GENRES_KEY)
            countriesValue = it.getString(COUNTRIES_VALUE)
            genresValue = it.getString(GENRES_VALUE)
            orderKey = it.getString(ORDER)
            typeKey = it.getString(TYPE)
            ratingFromKey = it.getInt(RATING_FROM)
            ratingToKey = it.getInt(RATING_TO)
            yearFromKey = it.getInt(YEAR_FROM)
            yearToKey = it.getInt(YEAR_TO)
            keywordsKey = it.getString(KEYWORDS)
            beenViewed = it.getBoolean(BEEN_VIEWED)
        }

        arguments?.let {
            countriesKey = it.getInt(COUNTRIES_KEY)
            genresKey = it.getInt(GENRES_KEY)
            countriesValue = it.getString(COUNTRIES_VALUE)
            genresValue = it.getString(GENRES_VALUE)
            orderKey = it.getString(ORDER)
            typeKey = it.getString(TYPE)
            ratingFromKey = it.getInt(RATING_FROM)
            ratingToKey = it.getInt(RATING_TO)
            yearFromKey = it.getInt(YEAR_FROM)
            yearToKey = it.getInt(YEAR_TO)
            keywordsKey = it.getString(KEYWORDS)
            beenViewed = it.getBoolean(BEEN_VIEWED)
        }

        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchParameterBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Film or serials switcher
        val buttonAll = binding.radio0.id
        val buttonFilm = binding.radio1.id
        val buttonSerial = binding.radio2.id

        when (typeKey) {
            "ALL" -> binding.filmSerialSwitcher.check(buttonAll)
            "FILM" -> binding.filmSerialSwitcher.check(buttonFilm)
            "TV_SERIES" -> binding.filmSerialSwitcher.check(buttonSerial)
        }

        binding.filmSerialSwitcher.setOnCheckedChangeListener { _, id ->
            typeKey = when (id) {
                buttonAll -> "ALL"
                buttonFilm -> "FILM"
                buttonSerial -> "TV_SERIES"
                else -> "UNKNOWN"
            }
        }

        // Выбор Страны
        binding.country.text =
            if (countriesValue == null) resources.getString(R.string.all_countries) else countriesValue
        binding.buttonOfCountry.setOnClickListener {
            val bundle = Bundle().apply {
                instance(this)
                putString(CONTENT_TYPE, TYPE_IS_COUNTRY)
            }
            findNavController().navigate(R.id.chooseParameterFragment, bundle)
        }

        // Выбор Жанра
        binding.genre.text = if (genresValue == null) resources.getString(R.string.all_genres) else genresValue
        binding.buttonOfGenre.setOnClickListener {
            val bundle = Bundle().apply {
                instance(this)
                putString(CONTENT_TYPE, TYPE_IS_GENRE)
            }
            findNavController().navigate(R.id.chooseParameterFragment, bundle)
        }

        //Выбор даты
        val dateText = "c $yearFromKey до $yearToKey"
        binding.year.text = dateText
        binding.buttonOfYear.setOnClickListener {
            val bundle = Bundle().apply {
                instance(this)
            }
            findNavController().navigate(R.id.chooseDateFragment, bundle)
        }

        //Rating range slider
        binding.rangeSlider.values = listOf(ratingFromKey?.toFloat(), ratingToKey?.toFloat())
        binding.rangeSlider.addOnChangeListener { _, _, _ ->
            val values = binding.rangeSlider.values
            ratingFromKey = values[0].toInt()
            ratingToKey = values[1].toInt()
            binding.rate.text = "от $ratingFromKey до $ratingToKey"
        }

        // Sorting switcher
        val buttonDate = binding.sortByDate.id
        val buttonPop = binding.sortByPopular.id
        val buttonRate = binding.sortByRate.id

        when (orderKey) {
            "YEAR" -> binding.sortingSwitcher.check(buttonDate)
            "NUM_VOTE" -> binding.sortingSwitcher.check(buttonPop)
            "RATING" -> binding.sortingSwitcher.check(buttonRate)
        }

        binding.sortingSwitcher.setOnCheckedChangeListener { _, id ->
            orderKey = when (id) {
                buttonDate -> "YEAR"
                buttonPop -> "NUM_VOTE"
                buttonRate -> "RATING"
                else -> "UNKNOWN"
            }
        }

        // Показывать/не показывать просмотренное
        binding.titleViewed.text =
            if (beenViewed) resources.getString(R.string.viewed) else resources.getString(R.string.unviewed)
        binding.buttonOfViewed.setOnClickListener {
            beenViewed = !beenViewed
            if (beenViewed) {
                binding.icViewed.background = ResourcesCompat.getDrawable(resources, R.drawable.ic_viewed, null)
                binding.titleViewed.text = resources.getString(R.string.viewed)
            } else {
                binding.icViewed.background = ResourcesCompat.getDrawable(resources, R.drawable.ic_unviewed, null)
                binding.titleViewed.text = resources.getString(R.string.unviewed)
            }
        }

        //переход по анажатию кнопки
        binding.buttonBack.setOnClickListener {
            val bundle = Bundle().apply { instance(this) }
            findNavController().navigate(R.id.action_searchParameterFragment_to_navigation_search, bundle)
        }

        //перехват нажатия кнопки назад андройда
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val bundle = Bundle().apply { instance(this) }
                    findNavController().navigate(R.id.action_searchParameterFragment_to_navigation_search, bundle)
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        val bottomHandle = (activity as BottomBarHandle)
        bottomHandle.hideBottomBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instance(outState)
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
        const val TYPE_IS_GENRE = "Жанр"
        const val CONTENT_TYPE = "content_type"
    }
}
