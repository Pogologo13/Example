package com.app.skillcinema.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.app.skillcinema.R
import com.app.skillcinema.data.retrofit.FilmItem
import com.app.skillcinema.data.retrofit.HumanDetailDto
import com.app.skillcinema.data.room.SavedListWithItem
import com.app.skillcinema.databinding.DialogNameCollectionBinding
import com.app.skillcinema.databinding.FragmentProfileBinding
import com.app.skillcinema.ui.detail.SimilarAdapter
import com.app.skillcinema.ui.home.HumanDetailAdapter
import com.app.skillcinema.utils.BottomBarHandle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

const val KINOPOISK_ID = "kinopoisk_id"
private const val STUFF_ID = "stuff_id"
private const val SAVED_LIST_NAME = "saved_list_name"


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mainViewModelFactory: ProfileViewModelFactory

    private val viewModel: ProfileViewModel by viewModels { mainViewModelFactory }

    private lateinit var textDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    textDialog = Dialog(requireContext())

                    viewModel.isCreated.collect {
                        if (it) textDialog.dismiss()
                    }

                }

                launch {
                    viewModel.getViewedFilms().collect {
                        if (!it.isNullOrEmpty()) {
                            binding.viewedCount.text = it.size.toString()
                            val viewedAdapter = SimilarAdapter({ filmItem -> onClickFilm(filmItem) }, it)
                            binding.viewedRecycler.adapter = viewedAdapter
                        }
                    }
                }

                launch {
                    val flowFilms = viewModel.getInterestedFilms()
                    val flowHumans = viewModel.getInterestedHumans()
                    val filmList = emptyList<FilmItem>().toMutableList()
                    val humanList = emptyList<HumanDetailDto>().toMutableList()
                    merge(flowHumans, flowFilms).collect { mergesList ->
                        if (mergesList is List) {
                            mergesList.filterIsInstance<HumanDetailDto>().onEach { humanList.add(it) }
                            mergesList.filterIsInstance<FilmItem>().onEach { filmList.add(it) }

                        }
                        val filmAdapter = SimilarAdapter({ filmItem -> onClickFilm(filmItem) }, filmList)
                        val actorAdapter = HumanDetailAdapter({ human -> onClickHuman(human) }, humanList)
                        val concat = ConcatAdapter(filmAdapter, actorAdapter)
                        binding.interestedRecycler.adapter = concat
                        binding.interestedCount.text = mergesList?.size.toString()
                    }
                }

                launch {
                    viewModel.savedListAll.collect { allLists ->
                        val savedListAdapter =
                            ProfileDialogCollectionAdapter(
                                { listItem -> onCheckedSavedList(listItem) },
                                { onClickAll(it) },
                                allLists!!
                            )
                        binding.listRecycler.adapter = savedListAdapter
                    }

                }
            }
        }



        binding.createListCollection.setOnClickListener {
            onClickAddCollection()
        }
    }

    private fun onClickAll(savedListWithItems: SavedListWithItem) {
        if (savedListWithItems.savedItem?.isEmpty() == true) {
            Toast.makeText(requireContext(), "Список пуст", Toast.LENGTH_SHORT).show()
        } else {
            val savedListName = savedListWithItems.savedList.listItemName
            val bundle = Bundle().apply {
                putString(SAVED_LIST_NAME, savedListName)
            }
            findNavController().navigate(R.id.allSerialsFragment, bundle)
        }
    }

    private fun onCheckedSavedList(savedListName: String) {
        viewModel.deleteSavedList(savedListName)
    }

    private fun onClickHuman(humanItem: HumanDetailDto) {
        val bundle = Bundle().apply {
            putInt(STUFF_ID, humanItem.personId)
        }
        findNavController().navigate(R.id.actorDetailFragment, bundle)

    }

    private fun onClickFilm(filmItem: FilmItem) {
        val bundle = Bundle().apply {
            putInt(KINOPOISK_ID, filmItem.kinopoiskId)
        }
        findNavController().navigate(R.id.detailFragment, bundle)
    }

    private fun onClickAddCollection() {
        val nameDialogBinding = DialogNameCollectionBinding.inflate(layoutInflater)
        textDialog.setContentView(nameDialogBinding.root)
        textDialog.show()

        nameDialogBinding.buttonDissmiss.setOnClickListener {
            textDialog.dismiss()
        }

        nameDialogBinding.nameIsDoneButton.setOnClickListener {
            val newName = nameDialogBinding.newCollectionName.text.toString()
            if (newName.isNotBlank()) {
                viewModel.createSavedList(newName, requireContext())
            } else Toast.makeText(requireContext(), "Проверьте текст", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val bottomHandle = (activity as BottomBarHandle)
        bottomHandle.showBottomBar()
    }

}