package com.suvanl.fixmylinks.viewmodel.newruleflow.util

import com.suvanl.fixmylinks.domain.validation.Validator

class FakeDomainNameValidator : Validator {
    override fun isValid(domainName: String): Boolean {
        val basicDomainNameRegex = Regex("^([a-zA-Z0-9-]+\\.)*([a-zA-Z0-9-]{1,63}\\.[a-zA-Z]{2,})$")
        return domainName.matches(basicDomainNameRegex)
    }
}