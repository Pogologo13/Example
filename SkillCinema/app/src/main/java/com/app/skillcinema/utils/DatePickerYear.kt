package com.app.skillcinema.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.app.skillcinema.R
import com.app.skillcinema.databinding.DatePickerYearBinding


const val SPAN = 3
const val STEP = 11

class DatePickerYear
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var currentSelectYear = 2023
        private set
    private var currentFromYear: Int = 0
    private var currentToYear: Int = 0
    private var onClick: ((Int) -> Unit)? = null

    val binding = DatePickerYearBinding.inflate(LayoutInflater.from(context))

    fun go(year: Int) {
        currentSelectYear = year
        addView(binding.root)

        binding.years.columnCount = SPAN
        addTextView(currentSelectYear - STEP, currentSelectYear)
        checkedTextView(currentSelectYear)

        binding.left.setOnClickListener {
            binding.years.removeAllViews()
            addTextView(currentFromYear - STEP, currentFromYear)
            checkedTextView(currentSelectYear)
        }

        binding.right.setOnClickListener {
            binding.years.removeAllViews()
            addTextView(currentToYear, currentToYear + STEP)
            checkedTextView(currentSelectYear)
        }
    }

    private fun addTextView(fromYear: Int, toYear: Int) {
        for (year in fromYear..toYear) {
            val textView = createTextView(year)

            binding.years.addView(textView)
            textView.setOnClickListener {
                currentSelectYear = textView.id
                checkedTextView(textView)

                binding.years.children.filter { view -> view.id != textView.id }.forEach {
                    uncheckedTextView(it as TextView)
                }
                onClick?.invoke(currentSelectYear)
            }
        }

        binding.fromYearToYear.text = "$fromYear - $toYear"
        currentFromYear = fromYear
        currentToYear = toYear
    }

    private fun createTextView(
        year: Int
    ): TextView {
        val textView = TextView(binding.root.context)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        textView.setTextColor(resources.getColor(R.color.black, null))
        val params = LayoutParams(Gravity.FILL, ViewGroup.LayoutParams.WRAP_CONTENT)

        params.horizontalWeight = 1f

        params.leftMargin =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt()
        params.rightMargin =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt()

        params.bottomMargin =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt()
        textView.layoutParams = params

        textView.id = year
        textView.text = year.toString()
        textView.typeface = Typeface.DEFAULT_BOLD
        textView.gravity = Gravity.CENTER

        return textView
    }

    private fun checkedTextView(year: Int) {
        binding.years.children.find { view -> view.id == year }?.apply {
            checkedTextView(this as TextView)
        }
    }

    @SuppressLint("ResourceType")
    private fun checkedTextView(textView: TextView) {
        textView.background = ContextCompat.getDrawable(context, R.drawable.button_done)
        textView.setTextColor(resources.getColor(R.color.white, null))
    }

    private fun uncheckedTextView(textView: TextView) {
        textView.setBackgroundColor(resources.getColor(R.color.white, null))
        textView.setTextColor(resources.getColor(R.color.black, null))
    }

    fun setOnClickListener(onClick: (Int) -> Unit) {
        this.onClick = onClick
    }
}