package com.suvanl.fixmylinks.domain.validation

class ValidateRemovableParamsListUseCase {
    operator fun invoke(params: List<String>): ValidationResult {
        if (params.isEmpty()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Add at least one URL parameter name"
            )
        }

        return ValidationResult(isSuccessful = true)
    }
}