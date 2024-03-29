package com.example.userdata.presenters.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.userdata.R
import com.example.userdata.databinding.FragmentElementBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

// Аргументы для получения данных
private const val LOGIN = "login"

@AndroidEntryPoint
class ElementDetailFragment : Fragment() {
    //переменные для присвоения данных
    private var login: String? = null
    private var _binding: FragmentElementBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    private val viewModel: MainViewModel by viewModels { mainViewModelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        //анимация перехода
        val animationEnter = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.hange_image_transform)
        sharedElementEnterTransition = animationEnter


        arguments.let {
            login = it?.getString(LOGIN)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentElementBinding.inflate(layoutInflater)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("REST LOGIN", login.toString())
        login?.let { viewModel.getSingle(it) }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.singleUser.collect { user ->
                    if (user != null) {

                        Glide
                            .with(requireContext())
                            .load(user.avatar_url)
                            .into(binding.photo)
                        binding.nameText.text = "NAme: ${user.name}"
                        binding.dateText.text = "Date: ${user.name}"
                        binding.emailText.text = "email: ${user.email?: ""}"
                        binding.organization.text = "Org: ${user.organizations_url ?: ""}"
                        binding.followingCount.text = "Following: ${user.following}"
                        binding.followersCount.text = "Followers: ${user.followers}"
                    }
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}