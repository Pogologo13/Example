package com.app.skillcinema.ui.detail.photo_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.skillcinema.data.retrofit.Photo

private const val LIST_PHOTO = "list_photo"
private const val KINOPOISK_ID = "kinopoisk_id"

class AllPhotoViewPagerAdapter(private val kinopoiskId: Int, private val items: List<List<Photo>>, val fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = items.size
    override fun createFragment(position: Int): Fragment {
        val fragment = AllPhotViewPagerFragment()
        fragment.arguments = Bundle(). apply {
            putString(LIST_PHOTO, items[position].first().type)
            putInt(KINOPOISK_ID, kinopoiskId)
        }
        return fragment
    }
}