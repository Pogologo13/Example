package com.app.skillcinema.ui.detail.actor_detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.skillcinema.R
import com.app.skillcinema.data.retrofit.HumanItem
import com.app.skillcinema.databinding.FragmentAllHumanBinding
import com.app.skillcinema.ui.detail.ActorAdapter
import com.app.skillcinema.ui.detail.DetailViewModel
import com.app.skillcinema.ui.detail.DetailViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ALL_HUMANS = "all_humans"
private const val STUFF_ID = "stuff_id"


@AndroidEntryPoint
class AllHumanFragment : Fragment() {
    private var _binding: FragmentAllHumanBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mainViewModelFactory: DetailViewModelFactory

    private val viewModel: DetailViewModel by viewModels { mainViewModelFactory }
    private var _allHumans: Array<HumanItem>? = null
    private val allHumans get() = _allHumans?.toList()!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            @Suppress("DEPRECATION")
            _allHumans = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                it.getSerializable(ALL_HUMANS, Array<HumanItem>::class.java)
            } else it.getSerializable(ALL_HUMANS) as Array<HumanItem>

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentAllHumanBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ActorAdapter({human -> onClickHuman(human)},allHumans)
        binding.recyclerAllHuman.adapter = adapter
    }

    private fun onClickHuman(human: HumanItem) {
        val bundle = Bundle().apply {
            human.staffId?.let { putInt(STUFF_ID, it) }
        }
        viewModel.setHumanDetail(human.staffId!!)
        findNavController().navigate(R.id.actorDetailFragment, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}