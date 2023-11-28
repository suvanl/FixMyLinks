package com.suvanl.fixmylinks.ui.util

import android.util.Patterns
import com.suvanl.fixmylinks.domain.validation.Validator
import javax.inject.Inject

class DomainNameValidator @Inject constructor() : Validator {
    override fun isValid(domainName: String): Boolean {
        return Patterns.DOMAIN_NAME.matcher(domainName).matches()
    }
}