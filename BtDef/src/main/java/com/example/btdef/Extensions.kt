package com.example.btdef

import android.graphics.Color
import android.widget.ImageButton
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment

fun Fragment.changeButtonColor(button: ImageButton, color: Int){
    val drawable = button.drawable
    DrawableCompat.setTint(drawable, color)
    button.setImageDrawable(drawable)
}