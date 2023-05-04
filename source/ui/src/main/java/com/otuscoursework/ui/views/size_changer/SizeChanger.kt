package com.otuscoursework.ui.views.size_changer

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.otuscourcework.utils.enable
import com.otuscoursework.ui.databinding.ViewSizeChangerBinding
import kotlinx.parcelize.Parcelize

class SizeChanger @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var viewState = ViewState()

    private val binding =
        ViewSizeChangerBinding.inflate(LayoutInflater.from(context), this, true)

    fun setItemNames(names: List<String>) {
        viewState.names = names
        binding.apply {
            when (names.size) {
                MODE_SHORT -> {
                    radioLeft.text = names[INDEX_LEFT]
                    radioCenter.isVisible = false
                    radioRight.text = names[INDEX_CENTER]
                }
                MODE_BIG -> {
                    radioLeft.text = names[INDEX_LEFT]
                    radioCenter.text = names[INDEX_CENTER]
                    radioRight.text = names[INDEX_RIGHT]
                }
            }
        }
    }

    fun blockItem(index: Int, isEnabled: Boolean) {
        if (isEnabled) {
            if (viewState.blockedItems.contains(index)) viewState.blockedItems.remove(index)
        } else {
            if (!viewState.blockedItems.contains(index)) viewState.blockedItems.add(index)
        }

        binding.apply {
            when (index) {
                INDEX_LEFT -> radioLeft.enable(isEnabled)
                INDEX_CENTER -> radioCenter.enable(isEnabled)
                INDEX_RIGHT -> radioRight.enable(isEnabled)
            }
        }
    }

    fun setCheckedItem(index: Int = INDEX_CENTER) {
        viewState.selectedIndex = index
        binding.apply {
            radioLeft.isChecked = index == INDEX_LEFT
            radioCenter.isChecked = index == INDEX_CENTER
            radioRight.isChecked = index == INDEX_RIGHT
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle().apply {
            putParcelable(VIEW_STATE_TAG, viewState)
            putParcelable(BUNDLE_TAG, super.onSaveInstanceState())
        }
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            setState(state.getParcelable(VIEW_STATE_TAG)!!)
            super.onRestoreInstanceState(state.getParcelable(BUNDLE_TAG))
        }
    }

    private fun setState(newViewState: ViewState) {
        setItemNames(newViewState.names)
        setCheckedItem(newViewState.selectedIndex)
        newViewState.blockedItems.forEach { blockItem(it, false) }
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
        private const val VIEW_STATE_TAG = "view_state_tag"
        private const val BUNDLE_TAG = "bundle_tag"

        private const val MODE_BIG = 3
        private const val MODE_SHORT = 2

        const val INDEX_LEFT = 0
        const val INDEX_CENTER = 1
        const val INDEX_RIGHT = 2
    }

    @Parcelize
    private data class ViewState(
        var names: List<String> = listOf(),
        var selectedIndex: Int = INDEX_CENTER,
        var blockedItems: MutableList<Int> = mutableListOf()
    ) : Parcelable
}