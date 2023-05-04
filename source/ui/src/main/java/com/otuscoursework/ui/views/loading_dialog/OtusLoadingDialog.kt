package com.otuscoursework.ui.views.loading_dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.otuscoursework.ui.databinding.ViewLoadingDialogBinding

class OtusLoadingDialog : DialogFragment() {
    private lateinit var dialogBinding: ViewLoadingDialogBinding
    private lateinit var rotateAnimation: RotateAnimation

    var isShown = false
    private set

    init {
        initRotation()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            dialogBinding = ViewLoadingDialogBinding.inflate(requireActivity().layoutInflater)
            builder.setView(dialogBinding.root)
            dialogBinding.progress.animation = rotateAnimation
            val dialog = builder.create().also { dialog ->
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun initRotation() {
        rotateAnimation = RotateAnimation(
            START_ROTATE,
            END_ROTATE,
            Animation.RELATIVE_TO_SELF,
            AXIS_PIVOT,
            Animation.RELATIVE_TO_SELF,
            AXIS_PIVOT
        )
        rotateAnimation.duration = ANIMATION_DURATION
        rotateAnimation.interpolator = LinearInterpolator()
    }

    fun showDialog(fm: FragmentManager, tag: String) {
        isShown = true
        rotateAnimation.start()
        show(fm, tag)
    }

    override fun onDismiss(dialog: DialogInterface) {
        isShown = false
        rotateAnimation.cancel()
        super.onDismiss(dialog)
    }

    companion object {
        const val DIALOG_TAG = "otusLoadingDialog"

        private const val ANIMATION_DURATION = 5000L
        private const val AXIS_PIVOT = 0.5F
        private const val START_ROTATE = 0F
        private const val END_ROTATE = 359F
    }
}