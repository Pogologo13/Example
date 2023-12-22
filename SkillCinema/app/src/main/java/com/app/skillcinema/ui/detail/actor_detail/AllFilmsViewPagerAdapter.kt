package com.app.skillcinema.ui.detail.actor_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.skillcinema.data.retrofit.FilmWithActor


private const val LIST_FILM_WITH_ACTOR = "films_with_actor"
class AllFilmsViewPagerAdapter(private val items: List<List<FilmWithActor>>, val fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = items.size
    override fun createFragment(position: Int): Fragment {
        val fragment = AllFilmsWithActorViewPagerFragment()
        fragment.arguments = Bundle(). apply {
            putSerializable(LIST_FILM_WITH_ACTOR, items[position].toTypedArray())
        }
        return fragment
    }
}