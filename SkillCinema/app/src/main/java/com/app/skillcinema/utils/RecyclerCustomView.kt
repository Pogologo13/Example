package com.app.skillcinema.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.skillcinema.databinding.CustomRecyclerViewBinding

class RecyclerCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val binding: CustomRecyclerViewBinding = CustomRecyclerViewBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }


}