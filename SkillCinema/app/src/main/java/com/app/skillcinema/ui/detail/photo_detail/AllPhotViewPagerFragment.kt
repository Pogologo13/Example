package com.app.skillcinema.ui.detail.photo_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.skillcinema.databinding.FragmentAllPhotViewPagerBinding
import com.app.skillcinema.ui.detail.DetailViewModel
import com.app.skillcinema.ui.detail.DetailViewModelFactory
import com.app.skillcinema.ui.detail.PhotoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LIST_PHOTO = "list_photo"
private const val KINOPOISK_ID = "kinopoisk_id"

@AndroidEntryPoint
class AllPhotViewPagerFragment : Fragment() {

    private var _binding: FragmentAllPhotViewPagerBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mainViewModelFactory: DetailViewModelFactory
    private val viewModel: DetailViewModel by viewModels { mainViewModelFactory }

    private var photoListType: String? = null
    private var kinopoiskId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllPhotViewPagerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.takeIf { it.containsKey(LIST_PHOTO) }?.apply {
            photoListType = getString(LIST_PHOTO)
            kinopoiskId = getInt(KINOPOISK_ID)
        }

        binding.loadIndicator.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                if ((kinopoiskId != null) && (photoListType != null)) {
                    viewModel.getPhotoByType(kinopoiskId!!, photoListType!!).collect { photoList ->
                        val adapter = PhotoAdapter(photoList)
                        binding.recycler.adapter = adapter
                    }
                }
            }
        }

    }


}