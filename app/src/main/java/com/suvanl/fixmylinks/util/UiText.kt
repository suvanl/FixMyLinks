package com.suvanl.fixmylinks.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    /**
     * Represents a string that can change based on certain conditions, such as a string from an
     * API response.
     */
    data class DynamicString(val value: String) : UiText()

    /**
     * Represents an Android string resource.
     */
    class StringResource(@StringRes val id: Int, vararg val args: Any) : UiText()

    /**
     * Returns the text as a string that can be used in the UI
     */
    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(id, *args)
        }
    }
}
