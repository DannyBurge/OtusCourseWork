package com.otuscoursework.ui.fragments.menuItemDetail

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.otuscourcework.utils.setSafeOnClickListener
import com.otuscoursework.resource.R
import com.otuscoursework.resource.ResHelper
import com.otuscoursework.ui.databinding.FragmentMenuItemDetailBinding
import com.otuscoursework.ui.databinding.ViewBottomSheetBottomBinding
import com.otuscoursework.ui.navigation.CiceroneAppNavigator
import com.otuscoursework.ui.views.badge_button.ButtonType
import com.otuscoursework.ui.views.size_changer.SizeChanger
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.coroutines.launch
import java.io.Serializable
import java.lang.Integer.min
import javax.inject.Inject

@AndroidEntryPoint
class MenuItemDetailFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var ciceroneAppNavigator: CiceroneAppNavigator

    @Inject
    lateinit var resHelper: ResHelper

    private lateinit var menuItem: MenuItemDetailModel
    private var selectedSizeIndex: Int = SizeChanger.INDEX_CENTER
    private lateinit var changeFavouriteStatus: () -> Unit
    private lateinit var fragmentBinding: FragmentMenuItemDetailBinding

    private val viewModel: MenuItemDetailFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        menuItem = requireArguments().getParcelable(ITEM)!!
        changeFavouriteStatus =
            requireArguments().getSerializable(FAVOURITE_STATUS_CHANGE_UNIT) as () -> Unit
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding = FragmentMenuItemDetailBinding.inflate(layoutInflater, null, false)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initBottomSheetBehavior()
    }

    private fun initViews() {
        fragmentBinding.apply {
            fragmentTitle.text = menuItem.name
            descriptionInfo.text = menuItem.description

            ccalPart.text = menuItem.foodValue.ccal
            proteinsPart.text = menuItem.foodValue.proteins
            fatsPart.text = menuItem.foodValue.fats
            carbohydratesPart.text = menuItem.foodValue.carbohydrates

            Picasso.get()
                .load(menuItem.picture)
                .transform(
                    RoundedCornersTransformation(
                        requireContext().resources.getDimension(R.dimen.corner_radius).toInt(),
                        0,
                    )
                )
                .fit()
                .into(menuItemImage)
        }
        setFullFoodValue()
        initButtons()
    }

    private fun initButtons() {
        fragmentBinding.apply {
            backButton.apply {
                setButtonType(ButtonType.TOP)
                setSafeOnClickListener { changeBehaviourState() }
            }

            favouriteDetailButton.apply {
                setButtonType(ButtonType.FAVOURITE)
                setSafeOnClickListener { changeFavouriteStatus() }
            }
            showFavouriteIndicator()
        }
    }

    private fun changeBehaviourState() {
        if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            return
        }
        if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            return
        }

        checkBackButtonWithState()
    }

    private fun checkBackButtonWithState() {
        if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            fragmentBinding.backButton.setButtonType(ButtonType.DOWN)
        }
        if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            fragmentBinding.backButton.setButtonType(ButtonType.TOP)
        }
    }

    private fun changeFavouriteStatus() {
        menuItem.isInFavourite = !menuItem.isInFavourite
        changeFavouriteStatus.invoke()
        showFavouriteIndicator()
    }

    private fun addItemToCart() {
        lifecycleScope.launch {
            menuItem.amountInCart[selectedSizeIndex]++
            viewModel.addItemToCart(menuItem, selectedSizeIndex)
        }
    }

    private fun removeItemFromCart() {
        lifecycleScope.launch {
            menuItem.amountInCart[selectedSizeIndex]--
            viewModel.removeItemFromCart(menuItem, selectedSizeIndex)
        }
    }

    private fun showFavouriteIndicator() {
        fragmentBinding.favouriteDetailButton.showIndicator(menuItem.isInFavourite)
    }

    private lateinit var behavior: BottomSheetBehavior<View>
    private fun initBottomSheetBehavior() {
        val density = requireContext().resources.displayMetrics.density

        if (dialog == null) return

        val bottomSheet =
            dialog!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
        behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.peekHeight = (PEEK_HEIGHT * density).toInt()
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val coordinator =
            (dialog as BottomSheetDialog).findViewById<CoordinatorLayout>(com.google.android.material.R.id.coordinator)
        val containerLayout =
            dialog!!.findViewById<FrameLayout>(com.google.android.material.R.id.container)

        val bottomPart = ViewBottomSheetBottomBinding.inflate(dialog!!.layoutInflater)
        initBottomPart(bottomPart)

        bottomPart.root.layoutParams = FrameLayout.LayoutParams(
            min(behavior.maxWidth, resources.displayMetrics.widthPixels),
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        }
        containerLayout?.addView(bottomPart.root)

        var bottomSystemPanelHeight = 0
        ViewCompat.setOnApplyWindowInsetsListener(fragmentBinding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures())
            bottomSystemPanelHeight = insets.bottom
            WindowInsetsCompat.CONSUMED
        }

        fragmentBinding.root.updatePadding(bottom = bottomSystemPanelHeight)
        bottomPart.root.post {
            (coordinator?.layoutParams as ViewGroup.MarginLayoutParams).apply {
                bottomPart.root.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                bottomPart.root.updatePadding(bottom = bottomSystemPanelHeight)
                bottomMargin = bottomPart.root.height + bottomSystemPanelHeight
                containerLayout?.requestLayout()
            }
        }

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                checkBackButtonWithState()
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun initBottomPart(bottomPart: ViewBottomSheetBottomBinding) {
        bottomPart.apply {
            sizeChanger.apply {
                setItemNames(menuItem.subItems.map { it.displayName })
                setCheckedItem(index = selectedSizeIndex)
                setOnCheckedChangeListener {
                    selectedSizeIndex = it
                    addRemoveButton.setCounter(menuItem.amountInCart[selectedSizeIndex])
                    setFullFoodValue()
                }
            }
            addRemoveButton.apply {
                setCounter(menuItem.amountInCart[selectedSizeIndex])
                setOnPlusButtonCallback {
                    addItemToCart()
                    setCounter(menuItem.amountInCart[selectedSizeIndex])
                }
                setOnMinusButtonCallback {
                    removeItemFromCart()
                    setCounter(menuItem.amountInCart[selectedSizeIndex])
                }
            }
            toCartButton.setSafeOnClickListener {
                ciceroneAppNavigator.toCartScreen()
                this@MenuItemDetailFragment.dismiss()
            }
        }
    }

    private fun setFullFoodValue() {
        fragmentBinding.apply {

            val weightRatio = (menuItem.subItems[selectedSizeIndex].weight).toFloat() / WEIGHT_100G

            weightFull.text = menuItem.subItems[selectedSizeIndex].weight.toString()
            ccalFull.text = (menuItem.foodValue.ccal.toInt() * weightRatio).toInt().toString()
            proteinsFull.text =
                (menuItem.foodValue.proteins.toInt() * weightRatio).toInt().toString()
            fatsFull.text = (menuItem.foodValue.fats.toInt() * weightRatio).toInt().toString()
            carbohydratesFull.text =
                (menuItem.foodValue.carbohydrates.toInt() * weightRatio).toInt().toString()
            price.text =
                getString(R.string.priceTagAccurate, menuItem.subItems[selectedSizeIndex].price)
        }
    }

    companion object {
        const val DIALOG_TAG = "detailDialog"

        const val WEIGHT_100G = 100F
        private const val PEEK_HEIGHT = 56

        private const val ITEM = "item"
        private const val FAVOURITE_STATUS_CHANGE_UNIT = "changeFavouriteUnit"
        fun newInstance(
            item: MenuItemDetailModel,
            changeFavouriteStatus: () -> Unit,
        ): MenuItemDetailFragment {
            val args = Bundle()

            args.putParcelable(ITEM, item)

            args.putSerializable(
                FAVOURITE_STATUS_CHANGE_UNIT,
                changeFavouriteStatus as Serializable
            )

            val fragment = MenuItemDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}