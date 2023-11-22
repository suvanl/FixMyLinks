package com.suvanl.fixmylinks.domain.validation

class ValidateDomainNameUseCase(private val validator: DomainNameValidator) {
    operator fun invoke(domainName: String): ValidationResult {
        if (domainName.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Domain name can't be blank"
            )
        }

        if (!validator.isValid(domainName)) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "That doesn't look like a valid domain name"
            )
        }

        return ValidationResult(isSuccessful = true)
    }
}