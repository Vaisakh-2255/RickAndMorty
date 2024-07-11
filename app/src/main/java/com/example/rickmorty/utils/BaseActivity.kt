package com.example.rickymorty.utils

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.pnikosis.materialishprogress.ProgressWheel

open class BaseActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null
    private var progress: ProgressWheel? = null
    private var isProgressShowing: Boolean = false
    private var dialog: Dialog? = null

    fun showAlert(message: String?, title: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setTitle(title)
            .setPositiveButton("Ok") { _, _ -> }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun getProgress(): ProgressWheel {
        if (progress == null) {
            progress = ProgressWheel(this)
        }
        return progress!!
    }

    fun showProgressWheel() {
        if (isProgressShowing) return // Avoid showing multiple progress wheels

        isProgressShowing = true
        val rootLayout = findViewById<View>(android.R.id.content) as? ViewGroup
        if (rootLayout != null) {
            rootLayout.removeView(getProgress()) // Remove if already added
            rootLayout.addView(getProgress())
        }
    }

    fun hideProgressWheel() {
        if (!isProgressShowing) return // No progress wheel is currently shown

        isProgressShowing = false
        val rootLayout = findViewById<View>(android.R.id.content) as? ViewGroup
        if (rootLayout != null) {
            rootLayout.removeView(getProgress())
        }
    }
}
