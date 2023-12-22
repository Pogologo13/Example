package com.app.skillcinema.ui.detail.seasons_detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.skillcinema.R
import com.app.skillcinema.data.retrofit.SerialItem
import com.app.skillcinema.databinding.FragmentViewpagerEpisodesLayoutBinding

private const val ARG_OBJECT = "object"
class DetailEpisodeViewPagerItemFragment : Fragment() {

    private var _binding: FragmentViewpagerEpisodesLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewpagerEpisodesLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            @Suppress("DEPRECATION") val item: SerialItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getSerializable(ARG_OBJECT, SerialItem::class.java)!!
            } else {
                getSerializable(ARG_OBJECT) as SerialItem
            }

            if(!item.episodes.isNullOrEmpty()) binding.loadIndicator.visibility = View.GONE
            val seasonQuantity = "${item.number} Сезон"
            val episodesQuantity =
                binding.root.resources.getQuantityString(
                    R.plurals.episodes,
                    item.episodes?.last()?.episodeNumber!!,
                    item.episodes.last().episodeNumber!!
                )
            val innerText = "$seasonQuantity, $episodesQuantity"
            binding.contEpisodes.text = innerText
            binding.recycler.adapter = EpisodeDetailRecyclerAdapter(item.episodes)

        }
    }
}