package com.otuscoursework.view.badge_button

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import com.otuscoursework.R
import com.otuscoursework.databinding.ViewRoundButtonBinding

class ButtonWithBadge @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewRoundButtonBinding.inflate(LayoutInflater.from(context), this, true)

    var counter: Int = 0
        private set

    init {
        updateBadge()
    }

    fun updateBadge(badgeValue: Int = 0) {
        counter = badgeValue
        binding.badgeNumber.apply {
            isVisible = badgeValue != 0
            text = badgeValue.toString()
        }
    }

    fun showIndicator(isVisible: Boolean = true) {
        binding.favouriteIndicator.isVisible = isVisible
    }

    fun setButtonType(typeButton: ButtonType) {
        binding.roundButtonIcon.setImageResource(
            when (typeButton) {
                ButtonType.TOP -> iconTop
                ButtonType.DOWN -> iconDown
                ButtonType.BACK -> iconBack
                ButtonType.CART -> iconCart
                ButtonType.NOTIFICATION -> iconNotification
                ButtonType.FAVOURITE -> iconHeart
                ButtonType.LIST -> iconList
                ButtonType.SALE -> iconSale
                ButtonType.ADD_TOKENS -> iconAddTokens
                ButtonType.TRASH -> iconTrash
            }
        )
    }

    companion object {
        private const val iconTop = R.drawable.icon_top
        private const val iconDown = R.drawable.icon_down
        private const val iconBack = R.drawable.icon_back
        private const val iconCart = R.drawable.icon_cart
        private const val iconNotification = R.drawable.icon_bell
        private const val iconHeart = R.drawable.icon_heart
        private const val iconList = R.drawable.icon_list
        private const val iconSale = R.drawable.icon_sale
        private const val iconAddTokens = R.drawable.icon_add_token
        private const val iconTrash = R.drawable.icon_trash
    }
}