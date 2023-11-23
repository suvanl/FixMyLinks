package com.suvanl.fixmylinks.ui.util

import android.util.Patterns
import com.suvanl.fixmylinks.domain.validation.Validator

class DomainNameValidator : Validator {
    override fun isValid(domainName: String): Boolean {
        return Patterns.DOMAIN_NAME.matcher(domainName).matches()
    }
}