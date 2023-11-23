package com.suvanl.fixmylinks.domain.validation

class ValidateUrlParamKeyUseCase {
    operator fun invoke(urlParamKey: String): ValidationResult {
        if (urlParamKey.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Parameter name can't be blank"
            )
        }

        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~"
        for (char in urlParamKey) {
            if (char !in allowedChars) {
                return ValidationResult(
                    isSuccessful = false,
                    errorMessage = "That doesn't look like a valid URL parameter name"
                )
            }
        }

        return ValidationResult(isSuccessful = true)
    }
}