package com.app.skillcinema.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.skillcinema.R
import com.app.skillcinema.data.retrofit.FilmItem
import com.app.skillcinema.databinding.FragmentHomeBinding
import com.app.skillcinema.ui.detail.actor_detail.FilmWithActorDetailAdapterWithBottomText
import com.app.skillcinema.utils.BottomBarHandle
import com.app.skillcinema.utils.RecyclerCustomView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.random.Random


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var setType = emptySet<String>().toMutableSet()
    private var date: String? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    private val viewModel: HomeViewModel by viewModels { homeViewModelFactory }
    private lateinit var commitScope: LifecycleCoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            setType = it.getStringArray(SET_TYPE)?.toMutableSet() ?: emptySet<String>().toMutableSet()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        commitScope = viewLifecycleOwner.lifecycleScope

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getBoolean().collect {
                        val firstTime = it == 0
                        if (firstTime) {
                            viewModel.setBoolean(1)
                            viewModel.setDefaultSaveList()
                            findNavController().navigate(R.id.bannerFragment)
                        }
                    }
                }

                launch {
                    val calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH).toString()
                    val monthRu = when (month) {
                        "January" -> "Премьеры Января"
                        "February" -> "Премьеры Февраля"
                        "March" -> "Премьеры Марта"
                        "April" -> "Премьеры Апреля"
                        "May" -> "Премьеры Мая"
                        "June" -> "Премьеры Июня"
                        "July" -> "Премьеры Июля"
                        "August" -> "Премьеры Августа"
                        "September" -> "Премьеры Сентября"
                        "October" -> "Премьеры Октября"
                        "November" -> "Премьеры Ноября"
                        else -> "Премьеры Декабря"
                    }
                    viewModel.setPremiere(year, month.uppercase())
                    date = "$year-$month"
                    binding.recycler1.binding.recycleTitle.text = monthRu
                    binding.recycler1.binding.buttonAll.setOnClickListener {
                        onClickButtonMore()
                    }

                    viewModel.premiereFilms.collect { films ->
                        val premiereAdapter =
                            films?.let {
                                binding.loadState.visibility = View.GONE
                                binding.scrollView.visibility = View.VISIBLE
                                FilmWithActorDetailAdapterWithBottomText(
                                    { filmCollection -> onClick(filmCollection) },
                                    it
                                )
                            }
                        binding.recycler1.binding.recycler.adapter = premiereAdapter
                    }
                }
            }
        }

        val listRecycler = listOf(
            binding.recycler2,
            binding.recycler3,
            binding.recycler4,
            binding.recycler5,
            binding.recycler6
        )

        while (setType.size != 5) {
            val digit = Random.nextInt(1, 9)
            when (digit) {
                1 -> setType.add(SERIALS)
                2 -> setType.add(CATASTROPHE_THEME)
                3 -> setType.add(TOP_POPULAR_ALL)
                4 -> setType.add(TOP_POPULAR_MOVIES)
                5 -> setType.add(TOP_250_MOVIES)
                6 -> setType.add(CLOSES_RELEASES)
                7 -> setType.add(LOVE_THEME)
                8 -> setType.add(FAMILY)
                else -> setType.add(KIDS_ANIMATION_THEME)
            }
        }

        val typeList = setType.toList()
        for (index in setType.indices) {
            getDataForRecycler(listRecycler[index], typeList[index])
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getDataForRecycler(
        recyclerView: RecyclerCustomView,
        type: String,
        isAllPage: Boolean = false
    ) {

        var getAllPage = isAllPage
        val pagedAdapter = FilmPagedAdapterWithTextBehavorPicture { filmCollection -> onClick(filmCollection) }
        val buttonAdapter = ButtonAdapter(onClickButton = { typing -> onClickButtonMore(typing) }, type = type)
        val concatAdapter = ConcatAdapter(pagedAdapter, buttonAdapter)

        recyclerView.binding.recycleTitle.text = when (type) {
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
        recyclerView.binding.recycler.adapter = concatAdapter
        commitTo(pagedAdapter, type, getAllPage)


        // click button logic
        recyclerView.binding.buttonAll.setOnClickListener {
            getAllPage = !getAllPage

            if (getAllPage) recyclerView.binding.buttonAll.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.purple_500)
            )
            else recyclerView.binding.buttonAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            commitScope.coroutineContext.cancelChildren()
            commitTo(pagedAdapter, type, getAllPage)
        }

        var firstentered = true
        // Прослушиватель изменений рксайклера
        pagedAdapter.addLoadStateListener {

            if (it.prepend.endOfPaginationReached) {

                if (getAllPage) { // если флаг изменившийся после нажатия кнопки "Все" тру
                    if (firstentered) {
                        recyclerView.binding.recycler.layoutManager?.smoothScrollToPosition(
                            recyclerView.binding.recycler,
                            RecyclerView.State(),
                            20
                        )
                        firstentered = false
                    }
                } else {
                    recyclerView.binding.recycler.layoutManager?.smoothScrollToPosition(
                        recyclerView.binding.recycler,
                        RecyclerView.State(),
                        0
                    )
                    firstentered = true
                }
            }
        }
    }

    private fun commitTo(pagedAdapter: FilmPagedAdapterWithTextBehavorPicture, type: String, getAllPage: Boolean) {
        commitScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getFilmCollections(type, getAllPage).collect { filmItem ->
                    pagedAdapter.submitData(filmItem)
                }
            }
        }
    }

    private fun onClick(filmItem: FilmItem) {
        val bundle = Bundle().apply {
            putInt(KINOPOISK_ID, filmItem.kinopoiskId)
            date?.let { putString(DATE, it) }
        }
        findNavController().navigate(R.id.detailFragment, bundle)
    }

    private fun onClickButtonMore(typing: String) {
        val bundle = Bundle().apply {
            putString(TYPE_OF_COLLECTION, typing)
        }
        findNavController().navigate(R.id.allSerialsFragment, bundle)
    }

    private fun onClickButtonMore() {
        val bundle = Bundle().apply {
            putString(DATE, date)
        }
        findNavController().navigate(R.id.allSerialsFragment, bundle)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArray(SET_TYPE, setType.toTypedArray())
    }

    override fun onResume() {
        super.onResume()
        val bottomHandle = (activity as BottomBarHandle)
        bottomHandle.showBottomBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val KINOPOISK_ID = "kinopoisk_id"
        const val TYPE_OF_COLLECTION = "type"
        const val DATE = "date"

        const val SET_TYPE = "list_type"
        const val SERIALS = "SERIALS"
        const val CATASTROPHE_THEME = "CATASTROPHE_THEME"
        const val TOP_POPULAR_ALL = "TOP_POPULAR_ALL"
        const val TOP_POPULAR_MOVIES = "TOP_POPULAR_MOVIES"
        const val TOP_250_MOVIES = "TOP_250_MOVIES"
        const val CLOSES_RELEASES = "CLOSES_RELEASES"
        const val LOVE_THEME = "LOVE_THEME"
        const val FAMILY = "FAMILY"
        const val KIDS_ANIMATION_THEME = "KIDS_ANIMATION_THEME"

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