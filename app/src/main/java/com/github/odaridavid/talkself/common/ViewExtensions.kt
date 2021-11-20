package com.github.odaridavid.talkself.common

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.odaridavid.talkself.R
import com.google.android.material.snackbar.Snackbar

fun View.displaySnackBar(
    message: String,
    length: Int = Snackbar.LENGTH_LONG,
    action: Snackbar.() -> Unit
) {
    Snackbar.make(this, message, length).apply {
        action()
    }.show()
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(ContextCompat.getColor(context, color)) }
}

fun ImageView.bindImage(context: Context, imageUrl: String?) {
    imageUrl?.let {
        Glide.with(context)
            .load(imageUrl)
            .circleCrop()
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .into(this)
    }
}
