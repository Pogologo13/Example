package com.app.skillcinema.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.skillcinema.R
import com.app.skillcinema.databinding.FragmentChooseDateBinding


class ChooseDateFragment : Fragment() {

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

    private var _binding: FragmentChooseDateBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseDateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.fromDatePicker.go(yearFromKey!!)
        binding.fromDatePicker.setOnClickListener {
            yearFromKey = binding.fromDatePicker.currentSelectYear
            if (yearFromKey!! > yearToKey!!) {
                binding.buttonDone.isEnabled = false
                binding.titleError.visibility = View.VISIBLE
            } else {
                binding.buttonDone.isEnabled = true
                binding.titleError.visibility = View.GONE
            }
        }

        binding.toDatePicker.go(yearToKey!!)
        binding.toDatePicker.setOnClickListener {
            yearToKey = binding.toDatePicker.currentSelectYear
            if (yearFromKey!! > yearToKey!!) {
                binding.buttonDone.isEnabled = false
                binding.titleError.visibility = View.VISIBLE
            } else {
                binding.buttonDone.isEnabled = true
                binding.titleError.visibility = View.GONE
            }
        }

        binding.buttonDone.setOnClickListener {
            val bundle = Bundle().apply {
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
            findNavController().navigate(R.id.searchParameterFragment, bundle)
        }
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
    }
}