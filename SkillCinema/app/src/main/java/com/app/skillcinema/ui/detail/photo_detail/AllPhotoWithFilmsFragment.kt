package com.app.skillcinema.ui.detail.photo_detail

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
import com.app.skillcinema.data.retrofit.Photo
import com.app.skillcinema.databinding.FragmentAllPhotoWithFilmsBinding
import com.app.skillcinema.ui.detail.DetailViewModel
import com.app.skillcinema.ui.detail.DetailViewModelFactory
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AllPhotoWithFilmsFragment : Fragment() {

    private var kinopoiskId: Int? = null
    private var _binding: FragmentAllPhotoWithFilmsBinding? = null
    val binding get() = _binding!!
    private var chipId = 0

    @Inject
    lateinit var detailViewModelFactory: DetailViewModelFactory
    private val viewModel: DetailViewModel by viewModels { detailViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            kinopoiskId = it.getInt(KINOPOISK_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllPhotoWithFilmsBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                kinopoiskId?.let { kinopoiskId ->

                    binding.buttonBack.setOnClickListener {
                        findNavController().navigateUp()
                    }

                    val still = viewModel.getPhotoByType(kinopoiskId, STILL).first()
                    val shooting = viewModel.getPhotoByType(kinopoiskId, SHOOTING).firstOrNull()
                    val wallpaper = viewModel.getPhotoByType(kinopoiskId, WALLPAPER).firstOrNull()
                    val cover = viewModel.getPhotoByType(kinopoiskId, COVER).firstOrNull()

                    val listPhoto = listOf(still, shooting, wallpaper, cover)

                    binding.chipGroup.apply {
                        isSingleSelection = true
                        isSelectionRequired = true
                    }

                    val filteredListPhoto = emptyList<List<Photo>>().toMutableList()

                    listPhoto.forEach {
                        if (!it.isNullOrEmpty()) {
                            createChip(it)
                            filteredListPhoto.add(it)
                        }
                    }

                    binding.chipGroup.check(0)

                    //листнер на нажатие на кнопку
                    binding.chipGroup.setOnCheckedStateChangeListener { group, _ ->
                        val checkedChipId: Int
                        if (group.checkedChipId == -1) return@setOnCheckedStateChangeListener
                        else {
                            //чип айди начинается с единицы, а счет станиц с ноля
                            checkedChipId = group.checkedChipId
                            binding.viewPager.currentItem = checkedChipId
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
                            if (positionOffset == 0.0f) binding.chipGroup.check(position)
                        }
                    })

                    // передаем во вью пейджер общий список профессий  и  текущий фрагмент
                    if (filteredListPhoto != emptyList<Photo>()) {
                        binding.viewPager.adapter =
                            AllPhotoViewPagerAdapter(kinopoiskId, filteredListPhoto, this@AllPhotoWithFilmsFragment)
                    }
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    fun createChip(listPhoto: List<Photo>) {
        //Создаем чип по каждому айтему в списке специализации

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

            text = when (listPhoto.first().type) {
                STILL -> "Кадры"
                SHOOTING -> "изображения со съемок"
                WALLPAPER -> "Обои"
                COVER -> "Обложки"
                else -> "Прочее"
            }
            id = chipId
            chipId++
        }
        binding.chipGroup.addView(chip)
    }

    private companion object {
        const val KINOPOISK_ID = "kinopoisk_id"
        const val STILL = "STILL"
        const val SHOOTING = "SHOOTING"
        const val WALLPAPER = "WALLPAPER"
        const val COVER = "COVER"
    }
}
