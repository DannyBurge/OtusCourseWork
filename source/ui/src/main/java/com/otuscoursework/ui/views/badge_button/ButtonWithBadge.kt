package com.otuscoursework.ui.views.badge_button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.otuscoursework.resource.R
import com.otuscoursework.ui.databinding.ViewRoundButtonBinding

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
        private val iconTop = R.drawable.icon_top
        private val iconDown = R.drawable.icon_down
        private val iconBack = R.drawable.icon_back
        private val iconCart = R.drawable.icon_cart
        private val iconNotification = R.drawable.icon_bell
        private val iconHeart = R.drawable.icon_heart
        private val iconList = R.drawable.icon_list
        private val iconSale = R.drawable.icon_sale
        private val iconAddTokens = R.drawable.icon_add_token
        private val iconTrash = R.drawable.icon_trash
    }
}