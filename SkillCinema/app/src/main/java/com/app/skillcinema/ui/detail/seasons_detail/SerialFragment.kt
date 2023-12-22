package com.app.skillcinema.ui.detail.seasons_detail

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.app.skillcinema.R
import com.app.skillcinema.databinding.FragmentSerialBinding
import com.app.skillcinema.ui.detail.DetailViewModel
import com.app.skillcinema.ui.detail.DetailViewModelFactory
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val KINOPOISK_ID = "kinopoisk_id"

@AndroidEntryPoint
class SerialFragment : Fragment() {
    private var kinopoiskIdFromDetailFragment: Int? = null
    private var _binding: FragmentSerialBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mainViewModelFactory: DetailViewModelFactory

    private val viewModel: DetailViewModel by viewModels { mainViewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            kinopoiskIdFromDetailFragment = it.getInt(KINOPOISK_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSerialBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                kinopoiskIdFromDetailFragment?.let { viewModel.getSerial(it) }?.collect { serial ->
                    if (serial != null) {
                        binding.viewPager.adapter = ViewPagerAdapter(serial.items, this@SerialFragment)
                        binding.chipGroup.apply {
                            isSingleSelection = true
                            isSelectionRequired = true
                        }

                        //Создаем чип под каждый сезон
                        for (season in serial.items) {
                                val chip = Chip(requireContext()).apply {
                                    setLayoutParams(LayoutParams(200, LayoutParams.WRAP_CONTENT))
                                    isFocusable = true
                                    isCheckable = true
                                    isClickable = true
                                    isChecked = true
                                    isCheckedIconVisible = false
                                    chipStrokeWidth = 4F
                                    chipStrokeColor = ColorStateList.valueOf(R.color.black)
                                    setChipBackgroundColorResource(R.color.chip_background_selector)
                                    setTextAppearance(requireContext(), R.style.chip_text)
                                    textStartPadding = 75f
                                    text = season.number!!.toString()
                                    id = season.number
                                }
                                binding.chipGroup.addView(chip)
                        }
                        binding.chipGroup.check(1)

                        //листенер на нажатие чипа
                        binding.chipGroup.setOnCheckedStateChangeListener { group, _ ->
                            val checkedChipId: Int
                            if (group.checkedChipId == -1) return@setOnCheckedStateChangeListener
                            else {
                                checkedChipId = group.checkedChipId  //узнаем айди кнопки
                                binding.viewPager.currentItem = checkedChipId - 1 //перекдючаем страницу
                            }
                        }

                        //листенер на свайп страницы
                        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                            override fun onPageScrolled(
                                position: Int,
                                positionOffset: Float,
                                positionOffsetPixels: Int
                            ) {
                                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                                //проверка после окончания пролистывания
                                if (positionOffset == 0.0f) binding.chipGroup.check(position + 1)
                            }
                        })
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.chipGroup.removeAllViews()
        _binding = null
    }
}


