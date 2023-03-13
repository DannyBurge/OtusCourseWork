package com.otuscoursework.ui.fragments.menuItemDetail

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.otuscoursework.databinding.FragmentMenuItemDetailBinding
import com.otuscoursework.databinding.ViewBottomSheetBottomBinding
import com.otuscoursework.di.components.DaggerFragmentMenuItemDetailComponent
import com.otuscoursework.di.components.FragmentMenuItemDetailComponent
import com.otuscoursework.navigation.CiceroneAppNavigator
import com.otuscoursework.ui.CartKeeper
import com.otuscoursework.ui.main.MainActivity
import com.otuscoursework.ui.models.MenuItemModel
import com.otuscoursework.utils_and_ext.setSafeOnClickListener
import com.otuscoursework.view.badge_button.ButtonType
import com.otuscoursework.view.size_changer.SizeChanger
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable
import javax.inject.Inject


@AndroidEntryPoint
class MenuItemDetailFragment : BottomSheetDialogFragment() {
    private lateinit var menuItem: MenuItemModel
    private var selectedSizeIndex: Int = SizeChanger.INDEX_CENTER
    private lateinit var changeFavouriteStatus: () -> Unit
    private lateinit var addItemToCart: (Int) -> Unit
    private lateinit var removeItemFromCart: (Int) -> Unit
    private lateinit var fragmentBinding: FragmentMenuItemDetailBinding

    @Inject
    lateinit var cartKeeper: CartKeeper

    @Inject
    lateinit var ciceroneAppNavigator: CiceroneAppNavigator

    lateinit var fragmentComponent: FragmentMenuItemDetailComponent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentComponent = DaggerFragmentMenuItemDetailComponent
            .factory()
            .create(MainActivity.INSTANCE.activityComponent)

        menuItem = requireArguments().getParcelable(ITEM)!!
        selectedSizeIndex = requireArguments().getInt(SELECTED_SIZE)
        changeFavouriteStatus =
            requireArguments().getSerializable(FAVOURITE_STATUS_CHANGE_UNIT) as () -> Unit
        addItemToCart = requireArguments().getSerializable(ADD_TO_CART_UNIT) as (Int) -> Unit
        removeItemFromCart =
            requireArguments().getSerializable(REMOVE_FROM_CART_UNIT) as (Int) -> Unit
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentComponent.inject(this)
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
            menuItemName.text = menuItem.name
            descriptionInfo.text = menuItem.description

            weightPart.text = menuItem.foodValue.weight
            ccalPart.text = menuItem.foodValue.ccal
            proteinsPart.text = menuItem.foodValue.proteins
            fatsPart.text = menuItem.foodValue.fats
            carbohydratesPart.text = menuItem.foodValue.carbohydrates
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

            favouriteButton.apply {
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

    private fun showFavouriteIndicator() {
        fragmentBinding.favouriteButton.showIndicator(menuItem.isInFavourite)
    }

    private lateinit var behavior: BottomSheetBehavior<View>
    private fun initBottomSheetBehavior() {
        val density = requireContext().resources.displayMetrics.density

        if (dialog == null) return

        val bottomSheet =
            dialog!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
        behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.peekHeight = (COLLAPSED_HEIGHT * density).toInt()
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val coordinator =
            (dialog as BottomSheetDialog).findViewById<CoordinatorLayout>(com.google.android.material.R.id.coordinator)
        val containerLayout =
            dialog!!.findViewById<FrameLayout>(com.google.android.material.R.id.container)

        val bottomPart = ViewBottomSheetBottomBinding.inflate(dialog!!.layoutInflater)
        initBottomPart(bottomPart)

        bottomPart.root.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.BOTTOM
        }
        containerLayout?.addView(bottomPart.root)

        bottomPart.root.post {
            (coordinator?.layoutParams as ViewGroup.MarginLayoutParams).apply {
                bottomPart.root.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                bottomMargin = bottomPart.root.height
                containerLayout?.requestLayout()
            }
        }

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                checkBackButtonWithState()
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                    if (slideOffset > 0) {
//                        // Тут весь код, который уже был
//                    } else {
//                        // Коэффицент можно подобрать любой по желанию
//                        // А так же нужно предусмотреть случай, когда тянули вниз,
//                        //но начали возвращать наверх, но slideOffset ещё отрицательный. Для этого можно сравнивать прошлый и текущий slideOffset
//                        //и делать соответствующее решение
//                        bottomPart.y += slideOffset.absoluteValue * 5
//                        coordinator?.let {
//                            it.y += slideOffset.absoluteValue * 5
//                        }
//                    }
            }
        })
    }

    private fun initBottomPart(bottomPart: ViewBottomSheetBottomBinding) {
        bottomPart.apply {
            sizeChanger.apply {
                setItemNames(menuItem.sizes.map { it.displayName })
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
                    addItemToCart(selectedSizeIndex)
                    setCounter(menuItem.amountInCart[selectedSizeIndex])
                }
                setOnMinusButtonCallback {
                    removeItemFromCart(selectedSizeIndex)
                    setCounter(menuItem.amountInCart[selectedSizeIndex])
                }
            }
        }
    }

    private fun setFullFoodValue() {
        selectedSizeIndex

        fragmentBinding.apply {
            weightFull.text = menuItem.sizes[selectedSizeIndex].foodValue.weight
            ccalFull.text = menuItem.sizes[selectedSizeIndex].foodValue.ccal
            proteinsFull.text = menuItem.sizes[selectedSizeIndex].foodValue.proteins
            fatsFull.text = menuItem.sizes[selectedSizeIndex].foodValue.fats
            carbohydratesFull.text = menuItem.sizes[selectedSizeIndex].foodValue.carbohydrates
            price.text = "₽ ${menuItem.sizes[selectedSizeIndex].price}"
        }
    }

    companion object {
        private const val COLLAPSED_HEIGHT = 56
        const val DIALOG_TAG = "detailDialog"

        private const val ITEM = "item"
        private const val FAVOURITE_STATUS_CHANGE_UNIT = "changeFavouriteUnit"
        private const val ADD_TO_CART_UNIT = "addToCartUnit"
        private const val REMOVE_FROM_CART_UNIT = "removeFromCart"
        private const val SELECTED_SIZE = "selectedSize"
        fun newInstance(
            item: MenuItemModel,
            index: Int,
            changeFavouriteStatus: () -> Unit,
            addItemToCart: (Int) -> Unit,
            removeItemFromCart: (Int) -> Unit
        ): MenuItemDetailFragment {
            val args = Bundle()

            args.putParcelable(ITEM, item)
            args.putInt(SELECTED_SIZE, index)

            args.putSerializable(
                FAVOURITE_STATUS_CHANGE_UNIT,
                changeFavouriteStatus as Serializable
            )
            args.putSerializable(ADD_TO_CART_UNIT, addItemToCart as Serializable)
            args.putSerializable(REMOVE_FROM_CART_UNIT, removeItemFromCart as Serializable)

            val fragment = MenuItemDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}