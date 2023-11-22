package com.suvanl.fixmylinks.domain.validation

interface DomainNameValidator {
    fun isValid(domainName: String): Boolean
}