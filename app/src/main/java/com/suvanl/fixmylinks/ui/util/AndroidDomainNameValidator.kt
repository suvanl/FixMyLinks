package com.suvanl.fixmylinks.ui.util

import android.util.Patterns
import com.suvanl.fixmylinks.domain.validation.DomainNameValidator

class AndroidDomainNameValidator : DomainNameValidator {
    override fun isValid(domainName: String): Boolean {
        return Patterns.DOMAIN_NAME.matcher(domainName).matches()
    }
}