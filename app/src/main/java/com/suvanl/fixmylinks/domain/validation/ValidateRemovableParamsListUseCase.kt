package com.suvanl.fixmylinks.domain.validation

import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.util.UiText
import javax.inject.Inject

class ValidateRemovableParamsListUseCase @Inject constructor() {
    operator fun invoke(params: List<String>): ValidationResult {
        if (params.isEmpty()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = UiText.StringResource(R.string.add_at_least_one_param_name)
            )
        }

        return ValidationResult(isSuccessful = true)
    }
}