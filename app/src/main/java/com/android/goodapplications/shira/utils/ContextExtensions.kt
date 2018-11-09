package com.android.goodapplications.shira.utils

import android.content.Context
import android.widget.Toast
import com.android.goodapplications.shira.R

fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Context.toast(messageInd: Int) =
        Toast.makeText(this, getString(messageInd), Toast.LENGTH_LONG).show()
