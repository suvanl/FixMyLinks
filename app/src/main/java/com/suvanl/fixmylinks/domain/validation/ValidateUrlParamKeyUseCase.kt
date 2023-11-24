package com.suvanl.fixmylinks.domain.validation

import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.util.UiText
import javax.inject.Inject

class ValidateUrlParamKeyUseCase @Inject constructor() {
    operator fun invoke(urlParamKey: String): ValidationResult {
        if (urlParamKey.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = UiText.StringResource(R.string.parameter_name_blank_error)
            )
        }

        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~"
        for (char in urlParamKey) {
            if (char !in allowedChars) {
                return ValidationResult(
                    isSuccessful = false,
                    errorMessage = UiText.StringResource(R.string.invalid_parameter_name)
                )
            }
        }

        return ValidationResult(isSuccessful = true)
    }
}