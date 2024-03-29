package com.example.userdata.presenters.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.userdata.R
import com.example.userdata.data.User
import com.example.userdata.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val LOGIN = "login"

@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    private val viewModel: MainViewModel by viewModels { mainViewModelFactory }

    //Подключение адаптера с коллбеком для функции onItemClick где аргументы:
    //серриализованый объект, вью (общий элемент анимации)
    private val pagedAdapter = MyPagedAdapter { photo, view -> onItemClick(photo, view) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Ресайклеру назначаем адаптер с вложеным лоад стейт адаптером
        binding.recycler.adapter = pagedAdapter.withLoadStateFooter(MyLoadStateAdapter())

        // обноаление данных на экране, начиная с 1 страницы(указано в ф-ции рефреш адаптера)
        binding.swipeRefresh.setOnRefreshListener {
            pagedAdapter.refresh()
        }

        //отслеживаем состояние загрузки для индикации прогресс бара
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                pagedAdapter.loadStateFlow.collect {
                    binding.swipeRefresh.isRefreshing = it.refresh == LoadState.Loading
                }
            }
        }

        //подписываемся на пейджер и отпраляем данные в адаптер
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pagedUser().collect { userData ->
                    pagedAdapter.submitData(userData)
                }
            }
        }
    }

    //при нажатии на лемент ресайклера создаем бандл с данными и общим вью и
    //передаем в Элеменет детейл фрагмент
    private fun onItemClick(item: User, view: ImageView) {
        val bundle = Bundle().apply {
            putString(LOGIN, item.login)
        }

        val extras = FragmentNavigatorExtras(view to view.transitionName)
        findNavController().navigate(
            R.id.action_mainFragment_to_elementFragment, bundle, null, extras
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}