package com.app.skillcinema.ui.detail.seasons_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.skillcinema.data.retrofit.SerialItem

private const val ARG_OBJECT = "object"
class ViewPagerAdapter(private val items: List<SerialItem>, val fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = items.size
    override fun createFragment(position: Int): Fragment {
        val fragment = DetailEpisodeViewPagerItemFragment()
        fragment.arguments = Bundle(). apply {
            putSerializable(ARG_OBJECT, items[position])
        }
        return fragment
    }


}

