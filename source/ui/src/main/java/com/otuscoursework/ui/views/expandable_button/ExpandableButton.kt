package com.otuscoursework.ui.views.expandable_button

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.otuscourcework.utils.setSafeOnClickListener
import com.otuscoursework.ui.databinding.ViewExpandableButtonBinding

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

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle().apply {
            putInt(COUNTER_TAG, counter)
            putParcelable(BUNDLE_TAG, super.onSaveInstanceState())
        }
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            setCounter(state.getInt(COUNTER_TAG))
            super.onRestoreInstanceState(state.getParcelable(BUNDLE_TAG))
        }
    }

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

    companion object {
        private const val COUNTER_TAG = "counter_tag"
        private const val BUNDLE_TAG = "bundle_tag"
    }
}