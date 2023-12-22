package com.app.skillcinema.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.skillcinema.databinding.ViewLoadBinding


class LoadView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {


    private val binding = ViewLoadBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }


}