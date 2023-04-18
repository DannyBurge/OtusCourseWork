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
import com.otuscoursework.ui.databinding.ViewLoadingDialogBinding
import com.otuscoursework.ui.main.MainActivity

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
            0f,
            359f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnimation.duration = 5000
        rotateAnimation.interpolator = LinearInterpolator()

    }

    fun show() {
        isShown = true
        rotateAnimation.start()
        this.show(MainActivity.INSTANCE.supportFragmentManager, DIALOG_TAG)
    }

    override fun onDismiss(dialog: DialogInterface) {
        isShown = false
        rotateAnimation.cancel()
        super.onDismiss(dialog)
    }

    companion object {
        const val DIALOG_TAG = "otusLoadingDialog"
    }
}