package com.hakancevik.newsappbihaber.util

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.hakancevik.newsappbihaber.R


fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}


fun Activity.customToast(toastMessage: String) {
    val inflater = this.layoutInflater
    val toast = Toast(this)
    toast.duration = Toast.LENGTH_SHORT
    val layout = inflater.inflate(
        R.layout.custom_toast,
        this.findViewById(R.id.custom_toast_constraint)
    )
    val toastTextView: TextView = layout.findViewById<View>(R.id.custom_toast_text) as TextView
    toastTextView.text = toastMessage
    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
    toast.view = layout
    toast.show()
}