package com.otuscoursework.view.badge_button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
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

    fun setButtonType(typeButton: ButtonType) {
        binding.roundButtonIcon.setImageResource(
            when (typeButton) {
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
        if (typeButton == ButtonType.SALE || typeButton == ButtonType.ADD_TOKENS || typeButton == ButtonType.NOTIFICATION) {
            binding.apply {
                root.layoutParams = LayoutParams(
                    resources.getDimension(R.dimen.roundButtonBgSizeXXLarge).toInt(),
                    resources.getDimension(R.dimen.roundButtonBgSizeXXLarge).toInt()
                )
                roundButtonIcon.scaleX = 1.4F
                roundButtonIcon.scaleY = 1.4F
            }
        }
    }

    private val iconBack = R.drawable.icon_back
    private val iconCart = R.drawable.icon_cart
    private val iconNotification = R.drawable.icon_bell
    private val iconHeart = R.drawable.icon_heart
    private val iconList = R.drawable.icon_list
    private val iconSale = R.drawable.icon_sale
    private val iconAddTokens = R.drawable.icon_add_token
    private val iconTrash = R.drawable.icon_trash
}