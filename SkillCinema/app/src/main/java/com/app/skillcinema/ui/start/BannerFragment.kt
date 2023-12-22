package com.app.skillcinema.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.skillcinema.R
import com.app.skillcinema.databinding.FragmentActivityStartBannerBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BannerFragment : Fragment(), AnimationListener {

    private var _binding: FragmentActivityStartBannerBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewFlipperBanner: ViewFlipper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,

        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityStartBannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSkip.setOnClickListener {
            findNavController().navigate(R.id.action_bannerFragment_to_navigation_home)
        }

        viewFlipperBanner = binding.flip.apply {
            isAutoStart = true
            setInAnimation(requireContext(), R.anim.in_flip)
            setOutAnimation(requireContext(),R.anim.out_flip)
            inAnimation.setAnimationListener(this@BannerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAnimationStart(animation: Animation?) = Unit

    override fun onAnimationEnd(animation: Animation?) {
        if (viewFlipperBanner.indexOfChild(viewFlipperBanner.currentView) == 2) {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(1000)
                viewFlipperBanner.stopFlipping()
                findNavController().navigate(R.id.action_bannerFragment_to_navigation_home)
            }
        }
    }

    override fun onAnimationRepeat(animation: Animation?) = Unit
}