package com.otuscoursework.ui.fragments.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.otuscoursework.R
import com.otuscoursework.databinding.FragmentDialogBinding
import com.otuscoursework.utils_and_ext.setSafeOnClickListener

class OtusDialogFragment() : DialogFragment() {

    lateinit var dialogType: DialogType

    lateinit var dialogBinding: FragmentDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogType = requireArguments().getSerializable(DIALOG_TYPE)!! as DialogType
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            dialogBinding = FragmentDialogBinding.inflate(requireActivity().layoutInflater)
            builder.setView(dialogBinding.root)
            dialogBinding.apply {
                dialogTitle.text = getString(
                    when (dialogType) {
                        DialogType.ADD_PROMO -> R.string.dialogAddPromoTitle
                        DialogType.ADD_ADDRESS -> R.string.dialogAddAddressTitle
                        DialogType.CLEAR_CART -> R.string.dialogClearCartTitle
                    }
                )
                applyButton.setSafeOnClickListener {
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