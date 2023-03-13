package com.otuscoursework.view.expandable_button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.otuscoursework.databinding.ViewExpandableButtonBinding
import com.otuscoursework.utils_and_ext.setSafeOnClickListener

typealias PlusButtonCallback = () -> Unit
typealias MinusButtonCallback = () -> Unit

class ExpandableButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding =
        ViewExpandableButtonBinding.inflate(LayoutInflater.from(context), this, true)

    private var counter: Int = 0

    fun setCounter(value: Int) {
        counter = value
        changeCounter()
    }

    fun setOnPlusButtonCallback(
        plusButtonCallback: PlusButtonCallback
    ) {
        binding.plusButton.setSafeOnClickListener {
            plusButtonCallback.invoke()
            changeCounter()
        }
    }

    fun setOnMinusButtonCallback(
        minusButtonCallback: MinusButtonCallback
    ) {
        binding.minusButton.setSafeOnClickListener {
            minusButtonCallback.invoke()
            changeCounter()
        }
    }

    private fun changeCounter() {
        binding.counterText.text = counter.toString()
        binding.minusContainer.isVisible = counter > 0
    }
}