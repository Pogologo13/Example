package com.app.skillcinema.ui.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.skillcinema.R
import com.app.skillcinema.data.retrofit.Country
import com.app.skillcinema.data.retrofit.FilmItem
import com.app.skillcinema.data.retrofit.Genre
import com.app.skillcinema.data.retrofit.HumanItem
import com.app.skillcinema.databinding.DialogNameCollectionBinding
import com.app.skillcinema.databinding.DialogPhotoLayoutBinding
import com.app.skillcinema.databinding.DialogSavedListBinding
import com.app.skillcinema.databinding.FragmentDetailBinding
import com.app.skillcinema.ui.detail.photo_detail.PhotoPagedAdapter
import com.app.skillcinema.utils.BottomBarHandle
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration


@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _kinopoiskId: Int? = null
    private val filmId get() = _kinopoiskId!!
    private var _binding: FragmentDetailBinding? = null
    private var filmObject: FilmItem? = null

    private val binding get() = _binding!!
    private var date: String? = null
    private lateinit var photoAdapter: PhotoPagedAdapter

    @Inject
    lateinit var mainViewModelFactory: DetailViewModelFactory
    private val viewModel: DetailViewModel by viewModels { mainViewModelFactory }
    private val savedListName = mutableListOf("Любимые", "Хочу посмотреть")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _kinopoiskId = it.getInt(KINOPOISK_ID)
            date = it.getString(DATE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoAdapter = PhotoPagedAdapter { position -> onClickPhoto(position) }
        binding.galleryRecycler.adapter = photoAdapter

        binding.buttonBack.setOnClickListener { findNavController().navigateUp() }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    filmId.let { viewModel.getItemFilm(it) }.collect { film ->
                        if (film != null) {
                            filmObject = film
                            if (film.premiereRu == null) date = null

                            viewModel.uploadFilm(
                                filmId,
                                date,
                                film.premiereRu,
                                film.serial
                            )


                            if (film.serial) setSerialData(film)

                            Glide
                                .with(requireContext())
                                .load(film.posterUrl)
                                .into(binding.poster)

                            //Название
                            binding.filmNameOrig.text = when {
                                film.nameOriginal != null -> film.nameOriginal
                                film.nameEn != null -> film.nameEn
                                else -> film.nameRu
                            }

                            //Рейтинг Кинопоиск
                            binding.filmRatio.text = film.ratingKinopoisk.let {
                                it.toString().replace("null", "") + " " + film.nameRu
                            }

                            //Жанры
                            binding.filmGenre.text = genresToString(film.year, film.genres)

                            //Страна
                            binding.filmCountry.text =
                                countriesRuntimeAgeLimitText(film.countries, film.filmLength, film.ratingAgeLimits)

                            //короткая аннотация
                            isVisible(binding.descriptionShort, film.shortDescription)

                            //Развернутая аннотация
                            val descriptionCount = film.description?.count() ?: 0
                            val cropDescription = if (descriptionCount > 250) film.description?.substring(
                                0,
                                250
                            ) + "..." else film.description
                            isVisible(binding.description, cropDescription)
                            var limitText = true
                            binding.description.setOnClickListener {
                                limitText = !limitText
                                if (limitText) {
                                    isVisible(binding.description, cropDescription)
                                } else
                                    isVisible(binding.description, film.description)
                            }


                            //кнопка добавить в избранное
                            var likeIsChecked = viewModel.buttonIsChecked(film.kinopoiskId, savedListName[0])
                            val like = binding.icLike
                            iconBackgroundChange(like, likeIsChecked, R.drawable.ic_like, R.drawable.ic_like_cheked)
                            like.setOnClickListener {
                                likeIsChecked = !likeIsChecked
                                iconBackgroundChange(like, likeIsChecked, R.drawable.ic_like, R.drawable.ic_like_cheked)
                                viewModel.updateInSavedList(likeIsChecked, film, savedListName[0])
                            }

                            //кнопка добавить в просмтореть похже
                            var markIsChecked = viewModel.buttonIsChecked(film.kinopoiskId, savedListName[1])
                            val mark = binding.icMark
                            iconBackgroundChange(mark, markIsChecked, R.drawable.ic_mark, R.drawable.ic_mark_checked)
                            mark.setOnClickListener {
                                markIsChecked = !markIsChecked
                                iconBackgroundChange(
                                    mark,
                                    markIsChecked,
                                    R.drawable.ic_mark,
                                    R.drawable.ic_mark_checked
                                )
                                viewModel.updateInSavedList(markIsChecked, film, savedListName[1])
                            }

                            //кнопка просмотренное
                            var viewedIsChecked = film.viewed
                            val viewed = binding.icViewed
                            iconBackgroundChange(viewed, viewedIsChecked, R.drawable.ic_unviewed, R.drawable.ic_viewed)
                            viewed.setOnClickListener {
                                viewedIsChecked = !viewedIsChecked
                                iconBackgroundChange(
                                    viewed,
                                    viewedIsChecked,
                                    R.drawable.ic_unviewed,
                                    R.drawable.ic_viewed
                                )
                                viewModel.updateFilm(film.apply { this.viewed = viewedIsChecked })
                            }

                            //кнопка поделиться ссылкой
                            binding.icShare.setOnClickListener {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "https://www.kinopoisk.ru/film/${film.kinopoiskId}/")
                                    type = "text/plain"
                                }

                                /** Не возвращается **/
                                startActivity(sendIntent)
//                                   share.launch(sendIntent)
                            }

                            binding.icTools.setOnClickListener {
                                getBottomDialog(film)
                            }
                        }
                    }
                }

                // загружаем данные в похожие фильмы
                launch {
                    val similarList = viewModel.getSimilar(filmId)
                    if (similarList == emptyList<FilmItem>()) {
                        binding.similarRecycler.visibility = View.GONE
                        binding.similar.text = "Похожих фильмов нет"
                    }
                    val adapter = SimilarAdapter({ filmItem -> onClickFilm(filmItem) }, similarList)
                    binding.similarRecycler.adapter = adapter
                }

                //
                launch {
                    viewModel.similarStateLoading.collect {
                        binding.similarProgress.visibility = if (it) View.VISIBLE else View.GONE
                    }
                }

                //Кнопка перехода на просморт изображений. Исчезает если изображений меньше 20
                launch {
                    viewModel.getGalleryById(filmId).collect { listPhoto ->
                        val countPhoto = listPhoto.size
                        binding.galleryButton.visibility = if (countPhoto > 20) View.VISIBLE else View.GONE
                        binding.galleryButton.text = countPhoto.toString()
                        binding.galleryButton.setOnClickListener {
                            val bundle = Bundle().apply { putInt(KINOPOISK_ID, filmId) }
                            findNavController().navigate(R.id.allPhotoWithFilmsFragment, bundle)
                        }
                    }
                }

                // Передаем изображения в адаптер галереи
                launch {
                    merge(
                        viewModel.getPhoto(filmId, STILL),
                        viewModel.getPhoto(filmId, SHOOTING),
                        viewModel.getPhoto(filmId, WALLPAPER),
                        viewModel.getPhoto(filmId, COVER),
                    ).collect {
                        photoAdapter.submitData(it)
                    }
                }
            }
        }

        //Инициалиризуем кнопки перехода на страницы с Актерами/неактерами
        // и ресайклеры к ним
        val buttonActors = binding.actorButton
        val recycleActors = binding.actorRecycler
        val buttonNotActors = binding.notActorButton
        val recyclerNotActors = binding.notActorRecycler

        //вызываем ф-2ию заполнеия актеров/не актеров
        getHumansList(20, recycleActors, buttonActors, true)
        getHumansList(6, recyclerNotActors, buttonNotActors, false)
    }


    private fun onClickPhoto(position: Int) {
        val dialog = Dialog(
            requireContext(),
            com.google.android.material.R.style.ThemeOverlay_Material3_MaterialCalendar_Fullscreen
        )
        val bindingDialog = DialogPhotoLayoutBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)

        val listPhoto = photoAdapter.snapshot().items
        val photoCount = photoAdapter.snapshot().items.size
        var nextPhotoPosition = position

        Glide
            .with(requireContext())
            .load(listPhoto[position].imageUrl)
            .into(bindingDialog.photo)

        dialog.show()

        bindingDialog.buttonDissmiss.setOnClickListener {
            dialog.dismiss()
        }

        //Следущее изображение
        bindingDialog.toRight.setOnClickListener {
            nextPhotoPosition++
            if (photoCount > nextPhotoPosition) {
                val nextPhoto = listPhoto[nextPhotoPosition]

                Glide
                    .with(requireContext())
                    .load(nextPhoto.imageUrl)
                    .into(bindingDialog.photo)

            } else nextPhotoPosition = photoCount
        }

        //Предыдущее изображение
        bindingDialog.toLeft.setOnClickListener {
            nextPhotoPosition--
            if (nextPhotoPosition > -1) {
                val nextPhoto = listPhoto[nextPhotoPosition]

                Glide
                    .with(requireContext())
                    .load(nextPhoto.imageUrl)
                    .into(bindingDialog.photo)

            } else nextPhotoPosition = 0
        }
    }


    private fun onClickFilm(film: FilmItem) {
        val bundle = Bundle().apply {
            putInt(KINOPOISK_ID, film.kinopoiskId)
        }
        findNavController().navigate(R.id.detailFragment, bundle)
    }


    private fun onClickAddCollection() {
        val textDialog = Dialog(requireContext())
        val nameDialogBinding = DialogNameCollectionBinding.inflate(layoutInflater)
        textDialog.setContentView(nameDialogBinding.root)
        textDialog.show()
        nameDialogBinding.buttonDissmiss.setOnClickListener {
            textDialog.dismiss()
        }

        //придумываем назкание
        nameDialogBinding.nameIsDoneButton.setOnClickListener {
            val newName = nameDialogBinding.newCollectionName.text.toString()
            if (newName.isNotBlank()) {

                //сверяем название с уже существующими
                //Если совпадений не, то сохраняем список
                viewLifecycleOwner.lifecycleScope.launch {
                    val list = emptyList<String>().toMutableList()
                    viewModel.savedListAll.first()?.forEach {
                        list.add(it.savedList.listItemName)
                    }
                    if (list.contains(newName)) {
                        Toast.makeText(requireContext(), "Список уже существует", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.createSavedList(newName)
                        filmObject?.let { it1 -> viewModel.updateInSavedList(true, it1, newName) }
                        textDialog.hide()
                    }
                }
            } else Toast.makeText(requireContext(), "Проверьте текст", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClickHuman(humanItem: HumanItem) {
        val bundle = Bundle().apply {
            humanItem.staffId?.let { putInt(STUFF_ID, it) }
            putString(DATE, date)
        }
        viewModel.setHumanDetail(humanItem.staffId!!)
        findNavController().navigate(R.id.actorDetailFragment, bundle)
    }

    override fun onResume() {
        super.onResume()
        (activity as BottomBarHandle).uncheckAllItemsInBottomMenu()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setSerialData(filmItem: FilmItem?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setSerial(filmId)
            viewModel.getSerial(filmItem?.kinopoiskId!!).collect { serial ->
                if (serial != null) {
                    val currentSeason = "${serial.items.first().number} сезон"
                    binding.filmGenre.gravity = Gravity.END
                    binding.serialSeasons.visibility = View.VISIBLE
                    binding.serialSeasons.text = currentSeason
                    binding.titleOfActor.text = "В сериале снимались"
                    binding.titleOfNotActor.text = "Над Сериалом работали"
                    binding.titleSeasonsSerial.visibility = View.VISIBLE
                    binding.titleSeasonsSerial.text = serial.total.toString()
                    binding.titleIsSerial.visibility = View.VISIBLE
                    binding.titleIsSerial.text = "Сезоны и серии"
                    binding.serialButton.visibility = View.VISIBLE

                    binding.titleSeasonsSerial.apply {
                        visibility = View.VISIBLE

                        //колчество сезонов
                        var seasons = 0
                        serial.items.forEach { seasons += it.episodes?.size!! }
                        val seasonQuantity =
                            resources.getQuantityString(R.plurals.seasons, serial.total, serial.total)

                        //количество серий
                        val episodeNumber = serial.items[0].episodes?.last()?.episodeNumber!!
                        val episodesQuantity =
                            resources.getQuantityString(R.plurals.episodes, episodeNumber, episodeNumber)
                        val innerText = "$seasonQuantity, $episodesQuantity"
                        text = innerText

                        binding.serialButton.setOnClickListener {
                            val bundle = Bundle().apply {
                                putInt(KINOPOISK_ID, filmItem.kinopoiskId)
                            }
                            findNavController().navigate(R.id.action_detailFragment_to_serialFragment, bundle)
                        }

                        binding.titleIsSerial.setOnClickListener {
                            val bundle = Bundle().apply {
                                putInt(KINOPOISK_ID, filmItem.kinopoiskId)
                            }
                            findNavController().navigate(R.id.action_detailFragment_to_serialFragment, bundle)
                        }
                    }
                }
            }
        }
    }

    private fun getBottomDialog(film: FilmItem) {
        val dialog = BottomSheetDialog(requireContext())
        val bindingDialog = DialogSavedListBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        bindingDialog.filmTitle.text = film.nameRu
        bindingDialog.dieTitle.text = film.ratingKinopoisk.toString()

        Glide
            .with(requireContext())
            .load(film.posterUrl)
            .into(bindingDialog.posterPreview)

        bindingDialog.buttonDissmiss.setOnClickListener {
            dialog.dismiss()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.savedListAll.collect { savedListWithItem ->
                val addAdapter = AddCollectionAdapter { onClickAddCollection() }
                val collectionAdapter =
                    DialogCollectionAdapter(
                        { isChecked, name -> onCheckedSavedList(isChecked, name) },
                        savedListWithItem!!, filmId
                    )
                val concatAdapter = ConcatAdapter(collectionAdapter, addAdapter)
                bindingDialog.recycler.adapter = concatAdapter
            }
        }

        dialog.show()
        dialog.setOnDismissListener {
            viewModel.updateFilm(film)
        }
    }

    private fun onCheckedSavedList(checked: Boolean, name: String) =
        filmObject?.let { viewModel.updateInSavedList(checked, it, name) }

    private fun isVisible(textView: TextView, text: String?) {
        if (text != null) {
            textView.text = text
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
        }
    }

    // сортировка на Актеров и не актеров, ограничение отображаемого списка
    private fun getHumansList(
        limit: Int, recycle: RecyclerView, button: TextView, isActor: Boolean
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                filmId.let { viewModel.getHumans(it) }.collect { humanList ->
                    if (humanList != emptyList<HumanItem>()) {
                        val humansAll = emptyList<HumanItem>().toMutableList()

                        humanList.forEach {
                            when {
                                isActor -> if (it?.professionKey == "ACTOR") it.let { human -> humansAll.add(human) }
                                !isActor -> if (it?.professionKey != "ACTOR") it?.let { human -> humansAll.add(human) }
                            }
                        }

                        val humans = if (humansAll.size > limit) humansAll.subList(0, limit) else humansAll
                        val adapter = ActorAdapter({ humanItem -> onClickHuman(humanItem) }, humans)
                        recycle.adapter = adapter
                        button.text = humansAll.size.toString()

                        when (isActor) {
                            true -> binding.titleOfActor.visibility =
                                if (humans.isEmpty()) View.GONE else View.VISIBLE

                            false -> binding.titleOfNotActor.visibility =
                                if (humans.isEmpty()) View.GONE else View.VISIBLE
                        }

                        if (humansAll.size < limit) button.visibility = View.GONE
                        else button.visibility = View.VISIBLE

                        button.setOnClickListener {
                            val bundle = Bundle().apply { putSerializable(ALL_HUMANS, humansAll.toTypedArray()) }
                            findNavController().navigate(R.id.allHumanFragment, bundle)
                        }
                    }
                }
            }
        }
    }

    private fun iconBackgroundChange(view: View, isChecked: Boolean, oldIcon: Int, newIcon: Int) {
        if (isChecked)
            view.background = ResourcesCompat.getDrawable(resources, newIcon, null)
        else view.background = ResourcesCompat.getDrawable(resources, oldIcon, null)

    }

    private fun genresToString(year: Int?, listGenre: List<Genre>?): String {
        var i = 0
        val genreString = StringBuilder()
        if (year != null) genreString.append("$year, ")
        listGenre?.forEach {
            genreString.append("${it.genre} ")
            if (listGenre.size - 1 > i) genreString.append(", ")
            ++i
        }
        return genreString.toString()
    }

    private fun countriesRuntimeAgeLimitText(countries: List<Country>?, time: String?, age: String?): String {
        val string = StringBuilder()
        countries?.forEach { string.append("${it.country}, ") }

        when {
            time.isNullOrBlank() -> Unit
            time.contains(":") -> string.append(time)
            else -> string.append(time.toInt().toDuration(DurationUnit.MINUTES)
                .toComponents { hours, minutes, _, _ -> "$hours ч $minutes мин," })
        }

        if (age != null) string.append(age.replace("age", " ").plus("+"))
        return string.toString()

    }

    private companion object {
        const val KINOPOISK_ID = "kinopoisk_id"
        const val STUFF_ID = "stuff_id"
        const val ALL_HUMANS = "all_humans"
        const val STILL = "STILL"
        const val SHOOTING = "SHOOTING"
        const val WALLPAPER = "WALLPAPER"
        const val COVER = "COVER"
        const val DATE = "date"
    }
}