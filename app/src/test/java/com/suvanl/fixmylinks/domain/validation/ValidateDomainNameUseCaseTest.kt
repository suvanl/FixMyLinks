package com.suvanl.fixmylinks.domain.validation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ValidateDomainNameUseCaseTest {

    private lateinit var validateDomainNameUseCase: ValidateDomainNameUseCase
    private lateinit var domainNameValidator: Validator

    @Before
    fun setup() {
        val basicDomainNameRegex = Regex("^([a-zA-Z0-9-]+\\.)*([a-zA-Z0-9-]{1,63}\\.[a-zA-Z]{2,})$")

        val mockDomainNameValidator = mock<Validator> {
            on { isValid(any()) } doAnswer { invocation ->
                val arg = invocation.getArgument<String>(0)
                arg.matches(basicDomainNameRegex)
            }
        }

        domainNameValidator = mockDomainNameValidator
        validateDomainNameUseCase = ValidateDomainNameUseCase(mockDomainNameValidator)
    }

    @Test
    fun `string containing only alphanumeric characters fails validation`() {
        val testDomainName = "thiscannotbearealdomainname"
        val result = validateDomainNameUseCase(testDomainName)

        verify(domainNameValidator).isValid(testDomainName)
        assertFalse(result.isSuccessful)
    }

    @Test
    fun `string containing alphanumeric characters and spaces fails validation`() {
        val testDomainName = "this cannot be a real domain name"

        val result = validateDomainNameUseCase(testDomainName)

        verify(domainNameValidator).isValid(testDomainName)
        assertFalse(result.isSuccessful)
    }

    @Test
    fun `domain name (with TLD) containing spaces fails validation`() {
        val testDomainName = "example site.com"
        val result = validateDomainNameUseCase(testDomainName)

        verify(domainNameValidator).isValid(testDomainName)
        assertFalse(result.isSuccessful)
    }

    @Test
    fun `domain name containing hyphen passes validation`() {
        val testDomainName = "example-site.com"
        val result = validateDomainNameUseCase(testDomainName)

        verify(domainNameValidator).isValid(testDomainName)
        assertTrue(result.isSuccessful)
    }

    @Test
    fun `domain name with multiple subdomains passes validation`() {
        val testDomainName = "this.domain.should.definitely.pass.validation.example.com"
        val result = validateDomainNameUseCase(testDomainName)

        verify(domainNameValidator).isValid(testDomainName)
        assertTrue(result.isSuccessful)
    }

    @Test
    fun `domain name containing slash fails validation validation`() {
        val testDomainName = "google.com/search"
        val result = validateDomainNameUseCase(testDomainName)

        verify(domainNameValidator).isValid(testDomainName)
        assertFalse(result.isSuccessful)
    }

    @Test
    fun `domain name containing invalid char (@) fails validation`() {
        val testDomainName = "test@example.com"
        val result = validateDomainNameUseCase(testDomainName)

        verify(domainNameValidator).isValid(testDomainName)
        assertFalse(result.isSuccessful)
    }

    @Test
    fun `domain name containing digit passes validation`() {
        val testDomainName = "www2.example.com"
        val result = validateDomainNameUseCase(testDomainName)

        verify(domainNameValidator).isValid(testDomainName)
        assertTrue(result.isSuccessful)
    }

    @Test
    fun `domain name containing multiple digits passes validation`() {
        val testDomainName = "www2.example2394928349840188563.com"
        val result = validateDomainNameUseCase(testDomainName)

        verify(domainNameValidator).isValid(testDomainName)
        assertTrue(result.isSuccessful)
    }
}