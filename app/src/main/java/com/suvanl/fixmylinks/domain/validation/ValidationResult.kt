package com.suvanl.fixmylinks.domain.validation

import com.suvanl.fixmylinks.util.UiText

data class ValidationResult(
    val isSuccessful: Boolean,
    val errorMessage: UiText.StringResource? = null,
)
