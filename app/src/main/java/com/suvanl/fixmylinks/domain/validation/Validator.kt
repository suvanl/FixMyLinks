package com.suvanl.fixmylinks.domain.validation

interface Validator {
    fun isValid(domainName: String): Boolean
}