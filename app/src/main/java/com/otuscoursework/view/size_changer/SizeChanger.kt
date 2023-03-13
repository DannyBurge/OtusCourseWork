package com.otuscoursework.view.size_changer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.otuscoursework.databinding.ViewSizeChangerBinding

class SizeChanger @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding =
        ViewSizeChangerBinding.inflate(LayoutInflater.from(context), this, true)

    fun setItemNames(names: List<String>) {
        binding.apply {
            radioLeft.text = names[INDEX_LEFT]
            radioCenter.text = names[INDEX_CENTER]
            radioRight.text = names[INDEX_RIGHT]
        }
    }

    fun setCheckedItem(index: Int = INDEX_CENTER) {
        binding.apply {
            radioLeft.isChecked = index == INDEX_LEFT
            radioCenter.isChecked = index == INDEX_CENTER
            radioRight.isChecked = index == INDEX_RIGHT
        }
    }

    fun setOnCheckedChangeListener(listener: (Int) -> Unit) {
        binding.apply {
            radioGroup.setOnCheckedChangeListener { _, _ ->
                val checkedRadioButton = listOf(
                    radioLeft.isChecked,
                    radioCenter.isChecked,
                    radioRight.isChecked,
                ).indexOf(true)

                listener.invoke(checkedRadioButton)
            }
        }
    }

    companion object {
        const val INDEX_LEFT = 0
        const val INDEX_CENTER = 1
        const val INDEX_RIGHT = 2
    }
}