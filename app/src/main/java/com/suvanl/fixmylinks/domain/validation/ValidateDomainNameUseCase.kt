package com.suvanl.fixmylinks.domain.validation

class ValidateDomainNameUseCase(private val domainNameValidator: Validator) {
    operator fun invoke(domainName: String): ValidationResult {
        if (domainName.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Domain name can't be blank"
            )
        }

        if (!domainNameValidator.isValid(domainName)) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "That doesn't look like a valid domain name"
            )
        }

        return ValidationResult(isSuccessful = true)
    }
}