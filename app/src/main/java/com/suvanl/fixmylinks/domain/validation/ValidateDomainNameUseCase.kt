package com.suvanl.fixmylinks.domain.validation

import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.util.UiText
import javax.inject.Inject

class ValidateDomainNameUseCase @Inject constructor(
    private val domainNameValidator: Validator
) {
    operator fun invoke(domainName: String): ValidationResult {
        if (domainName.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = UiText.StringResource(R.string.domain_name_blank_error)
            )
        }

        if (!domainNameValidator.isValid(domainName)) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = UiText.StringResource(R.string.invalid_domain_name)
            )
        }

        return ValidationResult(isSuccessful = true)
    }
}