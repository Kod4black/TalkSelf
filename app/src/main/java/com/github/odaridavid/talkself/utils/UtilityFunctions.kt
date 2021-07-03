package com.github.odaridavid.talkself.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import codes.side.andcolorpicker.converter.getBInt
import codes.side.andcolorpicker.converter.getGInt
import codes.side.andcolorpicker.converter.getRInt
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.odaridavid.talkself.R
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class UtilityFunctions {

    companion object {


        @SuppressLint("SimpleDateFormat")
        fun formatMillisecondsToTime(createdAt: Long): String {
            val format: DateFormat = SimpleDateFormat("HH:mm a")
            format.timeZone = TimeZone.getDefault()
            return format.format(createdAt)
        }

        @SuppressLint("SimpleDateFormat")
        fun formatMillisecondsToDate(createdAt: Long): String {
            val format: DateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm a")
            format.timeZone = TimeZone.getDefault()
            return format.format(createdAt)
        }

        fun Context.toast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        /**
         * Set up snackbar resources
         */
        inline fun View.snack(
            @StringRes messageRes: Int,
            length: Int = Snackbar.LENGTH_LONG,
            f: Snackbar.() -> Unit
        ) {
            snack(resources.getString(messageRes), length, f)
        }

        inline fun View.snack(
            message: String,
            length: Int = Snackbar.LENGTH_LONG,
            f: Snackbar.() -> Unit
        ) {
            val snack = Snackbar.make(this, message, length)
            snack.f()
            snack.show()
        }

        fun Snackbar.action(
            @StringRes actionRes: Int,
            color: Int? = null,
            listener: (View) -> Unit
        ) {
            action(view.resources.getString(actionRes), color, listener)
        }

        fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
            setAction(action, listener)
            color?.let { setActionTextColor(ContextCompat.getColor(context, color)) }
        }

        fun getColor(red: Int, green: Int, blue: Int, alpha: Double): String {

            var originalColor = java.lang.String.format("#%02x%02x%02x", red, green, blue)

            val alphaFixed = (alpha * 255).roundToInt()
            var alphaHex = java.lang.Long.toHexString(alphaFixed.toLong())

            if (alphaHex.length == 1) {
                alphaHex = "0$alphaHex"
            }

            originalColor = originalColor.replace("#", "#$alphaHex")

            return originalColor
        }

        fun <T> MutableLiveData<T>.notifyObserver() {
            this.value = this.value
        }

        fun Context.bindImage(imageUrl: String?, imageView: ImageView) {

            imageUrl?.let {
                Glide.with(imageView)
                    .load(imageUrl)
                    .circleCrop()
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(imageView)
            }


        }
    }

}
