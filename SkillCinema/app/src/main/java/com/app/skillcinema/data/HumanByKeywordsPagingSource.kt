package com.app.skillcinema.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.skillcinema.data.retrofit.HumanApi
import com.app.skillcinema.data.retrofit.HumanByKeywordDto
import com.app.skillcinema.data.retrofit.HumanItem
import com.app.skillcinema.utils.ApiKey
import javax.inject.Inject

private const val FIRST_PAGE = 1

class HumanByKeywordsPagingSource @Inject constructor(
    private val humanApi: HumanApi,
    private val localRepository: FilmLocalRepository,
    private val apiKey: ApiKey
) : PagingSource<Int, HumanItem>(
) {
    private lateinit var keywords: String
    override fun getRefreshKey(state: PagingState<Int, HumanItem>) = FIRST_PAGE
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HumanItem> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            getList(page, keywords)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    it.items,
                    prevKey = null,
                    nextKey = if(it.items.isNotEmpty()) page +1 else null
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }

    fun getHumansByKeywords(name: String) {
        keywords = name
    }

    private suspend fun getList(page: Int, words: String): HumanByKeywordDto {
        return runCatching {
            humanApi.getHumansByKeywords(apiKey.getKey(), words, page)
        }.fold(
            onSuccess = {
                localRepository.insertFoundedHumans(it)
                it
            },
            onFailure = {
                apiKey.changeKey()
                val item =  humanApi.getHumansByKeywords(
                    apiKey.getKey(), words, page
                )
                localRepository.insertFoundedHumans(item)
                item
            }
        )
    }
}