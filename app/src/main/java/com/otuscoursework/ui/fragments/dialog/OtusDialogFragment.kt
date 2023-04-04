package com.otuscoursework.ui.fragments.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.otuscoursework.R
import com.otuscoursework.databinding.FragmentDialogBinding
import com.otuscoursework.utils_and_ext.setSafeOnClickListener

class OtusDialogFragment : DialogFragment() {

    private lateinit var dialogType: DialogType

    private lateinit var dialogBinding: FragmentDialogBinding

    private lateinit var _positiveCallback: (String, String) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogType = requireArguments().getSerializable(DIALOG_TYPE)!! as DialogType
    }

    fun setPositiveCallback(positiveCallback: (String, String) -> Unit) {
        _positiveCallback = positiveCallback
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            dialogBinding = FragmentDialogBinding.inflate(requireActivity().layoutInflater)
            builder.setView(dialogBinding.root)
            dialogBinding.apply {
                when (dialogType) {
                    DialogType.ADD_PROMO -> {
                        dialogTitle.text = getString(R.string.dialogAddPromoTitle)
                        dialogBodyName.isVisible = false
                        dialogBodyName.hint = getString(R.string.dialogAddPromoValueHint)
                    }
                    DialogType.ADD_ADDRESS -> {
                        dialogTitle.text = getString(R.string.dialogAddAddressTitle)
                        dialogBodyName.isVisible = true
                        dialogBodyName.hint = getString(R.string.dialogAddAddressNameHint)
                        dialogBodyValue.hint = getString(R.string.dialogAddAddressValueHint)
                    }
                    DialogType.CLEAR_CART -> {
                        dialogTitle.text = getString(R.string.dialogClearCartTitle)
                        dialogBodyName.isVisible = false
                        dialogBodyValue.isVisible = false
                    }
                }
                applyButton.setSafeOnClickListener {
                    _positiveCallback.invoke(
                        dialogBinding.dialogBodyName.text.toString(),
                        dialogBinding.dialogBodyValue.text.toString()
                    )
                    dismiss()
                }
                cancelButton.setSafeOnClickListener {
                    dismiss()
                }
            }
            val dialog = builder.create().also { dialog ->
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val DIALOG_TAG = "otusDialog"
        private const val DIALOG_TYPE = "dialogType"

        fun newInstance(dialogType: DialogType): OtusDialogFragment {
            val args = Bundle()
            args.putSerializable(DIALOG_TYPE, dialogType)
            val fragment = OtusDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    enum class DialogType {
        ADD_PROMO,
        ADD_ADDRESS,
        CLEAR_CART
    }
}