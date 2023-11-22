package com.suvanl.fixmylinks.domain.validation

data class ValidationResult(
    val isSuccessful: Boolean,
    val errorMessage: String? = null,
)
