package com.otuscoursework.ui.views.popup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.otuscoursework.resource.R
import com.otuscoursework.ui.arch.recycler.BaseDelegationAdapter
import com.otuscoursework.ui.arch.recycler.RecyclerViewItem
import com.otuscoursework.ui.databinding.ViewPopupMenuBinding
import com.otuscoursework.ui.views.popup.ui_model.AddPopupUiItem
import com.otuscoursework.ui.views.popup.ui_model.PopupUiItem

class PopupMenuWithList(
    private val context: Context,
    private val view: View,
    header: String,
    itemList: List<RecyclerViewItem>
) {
    private val menuItemAdapter = createAdapter()
    var onPopupMenuItemClickListener: OnPopupMenuItemClickListener? = null
    var onAddPopupMenuItemClickListener: OnAddPopupMenuItemClickListener? = null

    private val popupBinding = ViewPopupMenuBinding.inflate(
        LayoutInflater.from(context),
        null,
        false
    )
    private val bodyWindow: PopupWindow = PopupWindow(
        popupBinding.root,
        ConstraintLayout.LayoutParams.WRAP_CONTENT,
        ConstraintLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        isFocusable = true
        elevation = context.resources.getDimension(R.dimen.elevation)
    }

    init {
        popupBinding.mode.text = header
        popupBinding.popupRecyclerView.adapter = menuItemAdapter
        popupBinding.popupRecyclerView.layoutManager = LinearLayoutManager(context)
        menuItemAdapter.items = itemList
        menuItemAdapter.notifyDataSetChanged()
    }

    fun show() {
        bodyWindow.showAsDropDown(
            view,
            0,
            -(view.height + (context.resources.getDimension(R.dimen.corner_radius) / 2).toInt())
        )
    }

    fun close() {
        bodyWindow.dismiss()
    }

    private fun onClickCallback(item: PopupUiItem) {
        onPopupMenuItemClickListener?.onPopupMenuItemClicked(item)
    }

    private fun onAddClickCallback(item: AddPopupUiItem) {
        onAddPopupMenuItemClickListener?.onAddPopupMenuItemClicked(item)
    }

    private fun createAdapter(): BaseDelegationAdapter {
        return BaseDelegationAdapter(
            PopupAdapterDelegate.popupItemDelegate(::onClickCallback),
            PopupAdapterDelegate.addAddressItemDelegate(::onAddClickCallback)
        )
    }

    interface OnPopupMenuItemClickListener {
        fun onPopupMenuItemClicked(item: PopupUiItem)
    }

    interface OnAddPopupMenuItemClickListener {
        fun onAddPopupMenuItemClicked(item: AddPopupUiItem)
    }
}