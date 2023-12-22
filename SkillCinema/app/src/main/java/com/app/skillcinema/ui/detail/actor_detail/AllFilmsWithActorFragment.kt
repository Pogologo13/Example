package com.app.skillcinema.ui.detail.actor_detail

import android.annotation.SuppressLint
import android.content.res.ColorStateList
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
import androidx.viewpager2.widget.ViewPager2
import com.app.skillcinema.R
import com.app.skillcinema.data.retrofit.FilmWithActor
import com.app.skillcinema.databinding.FragmentAllFilmsWithActorBinding
import com.app.skillcinema.ui.detail.DetailViewModel
import com.app.skillcinema.ui.detail.DetailViewModelFactory
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AllFilmsWithActorFragment : Fragment() {
    private var stuffId: Int? = null


    private var _binding: FragmentAllFilmsWithActorBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mainViewModelFactory: DetailViewModelFactory

    private val viewModel: DetailViewModel by viewModels { mainViewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            stuffId = it.getInt(HUMAN_ID)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllFilmsWithActorBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                stuffId?.let { stuff ->
                    viewModel.getHumanDetail(stuff).collect { humanDetail ->

                        //Имя Актера
                        binding.titleName.text = humanDetail?.nameRu ?: humanDetail?.nameEn

                        // выбираем оригинальные специализации из спимка
                        val distinctList = humanDetail?.films?.distinctBy { it.professionKey }
                        val filmListSortedByProfKey: MutableList<List<FilmWithActor>> =
                            emptyList<List<FilmWithActor>>().toMutableList()
                        if (distinctList != null) {

                            //для каждой специализации создаем список фильмов соответвующий этой специализации
                            for (item in distinctList) {
                                val list = emptyList<FilmWithActor>().toMutableList()
                                humanDetail.films.forEach {
                                    if (it.professionKey == item.professionKey) {
                                        list.add(it)
                                    }
                                }
                                //добавляем список с фильмами по конкретной специализации в общий список всех специализации
                                //количество чипов равно количесву специализаций в общем списке
                                if (list.size > 1) filmListSortedByProfKey.add(list)
                            }
                        }

                        binding.chipGroup.apply {
                            isSingleSelection = true
                            isSelectionRequired = true
                        }

                        //Создаем чип по каждому айтему в списке специализации
                        for (list in filmListSortedByProfKey) {
                            val chip = Chip(requireContext()).apply {
                                setLayoutParams(
                                    ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                    )
                                )
                                isFocusable = true
                                isCheckable = true
                                isClickable = true
                                isChecked = true
                                isCheckedIconVisible = false
                                chipStrokeWidth = 4F
                                chipStrokeColor = ColorStateList.valueOf(R.color.black)
                                setChipBackgroundColorResource(R.color.chip_background_selector)
                                setTextAppearance(requireContext(), R.style.chip_text)

                                val proffKey =
                                    if (!list.first().professionKey.isNullOrBlank())
                                        list.first().professionKey
                                    else list.last().professionKey

                                text = when (proffKey) {
                                    WRITER -> "Сценарист"
                                    HERSELF -> "Играет саму себя"
                                    HIMSELF -> "Играет самого себя"
                                    EDITOR -> "Редактор"
                                    COMPOSER -> "Композитор"
                                    HRONO_TITR_MALE -> "Закадровый голос"
                                    HRONO_TITR_FEMALE -> "Закадровый голос"
                                    ACTOR -> "Актер"
                                    DESIGN -> "художник по костюмам"
                                    DIRECTOR -> "Режиссер"
                                    VOICE_DIRECTOR -> "Звукорежиссер"
                                    else -> "Прочее"
                                }
                                id = filmListSortedByProfKey.indexOf(list) + 1
                            }
                            binding.chipGroup.addView(chip)
                        }
                        binding.chipGroup.check(1)

                        //листнер на нажатие на кнопку
                        binding.chipGroup.setOnCheckedStateChangeListener { group, _ ->
                            val checkedChipId: Int
                            if (group.checkedChipId == -1) return@setOnCheckedStateChangeListener
                            else {
                                //чип айди начинается с единицы, а счет станиц с ноля
                                checkedChipId = group.checkedChipId
                                binding.viewPager.currentItem = checkedChipId - 1
                            }
                        }

                        //коллбек на свайп
                        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                            override fun onPageScrolled(
                                position: Int,
                                positionOffset: Float,
                                positionOffsetPixels: Int
                            ) {
                                // счет страниц начинается с ноля, а чип айди с единицы
                                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                                if (positionOffset == 0.0f) binding.chipGroup.check(position + 1)
                            }
                        })

                        // передаем во вью пейджер общий список профессий  и  текущий фрагмент
                        binding.viewPager.adapter =
                            AllFilmsViewPagerAdapter(filmListSortedByProfKey, this@AllFilmsWithActorFragment)
                    }
                }
            }
        }
    }

    private companion object {
        const val WRITER = "WRITER"
        const val HERSELF = "HERSELF"
        const val HIMSELF = "HIMSELF"
        const val EDITOR = "EDITOR"
        const val COMPOSER = "COMPOSER"
        const val HRONO_TITR_MALE = "HRONO_TITR_MALE"
        const val HRONO_TITR_FEMALE = "HRONO_TITR_FEMALE"
        const val ACTOR = "ACTOR"
        const val DESIGN = "DESIGN"
        const val DIRECTOR = "DIRECTOR"
        const val VOICE_DIRECTOR = "VOICE_DIRECTOR"
        const val HUMAN_ID = "stuff_id"
    }
}