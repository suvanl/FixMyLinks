package com.suvanl.fixmylinks.util

import android.content.Context
import android.content.Intent

fun Context.shareTextContent(content: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)

    startActivity(shareIntent)
}