package com.suvanl.fixmylinks.domain.validation

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidateUrlParamKeyUseCaseTest {

    private lateinit var validateUrlParamKeyUseCase: ValidateUrlParamKeyUseCase

    @Before
    fun setup() {
        validateUrlParamKeyUseCase = ValidateUrlParamKeyUseCase()
    }

    @Test
    fun `url param key containing a single space is unsuccessful`() {
        val result = validateUrlParamKeyUseCase("hello world")
        assertEquals(false, result.isSuccessful)
    }

    @Test
    fun `url param key containing multiple spaces is unsuccessful`() {
        val result = validateUrlParamKeyUseCase("this is a url parameter key with spaces")
        assertEquals(false, result.isSuccessful)
    }

    @Test
    fun `url param key starting with space is unsuccessful`() {
        val result = validateUrlParamKeyUseCase(" something")
        assertEquals(false, result.isSuccessful)
    }

    @Test
    fun `url param key containing multiple invalid characters is unsuccessful`() {
        val result = validateUrlParamKeyUseCase(
            "this!doesnt#have@spaces\$but%is^still&invalid*because+of{chars}|like=this:"
        )
        assertEquals(false, result.isSuccessful)
    }

    @Test
    fun `url param key containing all uppercase letters is successful`() {
        val result = validateUrlParamKeyUseCase("ALLCAPSURLPARAMETER")
        assertEquals(true, result.isSuccessful)
    }

    @Test
    fun `url param key containing exclamation mark is unsuccessful`() {
        val result = validateUrlParamKeyUseCase("thisisinvalid!")
        assertEquals(false, result.isSuccessful)
    }

    @Test
    fun `url param key containing '@' sign is unsuccessful`() {
        val result = validateUrlParamKeyUseCase("user@server")
        assertEquals(false, result.isSuccessful)
    }

    @Test
    fun `url param key containing hash symbol (#) is unsuccessful`() {
        val result = validateUrlParamKeyUseCase("#android")
        assertEquals(false, result.isSuccessful)
    }

    @Test
    fun `url param key containing percent symbol is unsuccessful`() {
        val result = validateUrlParamKeyUseCase("hello%20fixmylinks")
        assertEquals(false, result.isSuccessful)
    }

    @Test
    fun `url param key containing caret (^) symbol is unsuccessful`() {
        val result = validateUrlParamKeyUseCase("value^2")
        assertEquals(false, result.isSuccessful)
    }

    @Test
    fun `url param key containing ampersand (&) symbol is unsuccessful`() {
        val result = validateUrlParamKeyUseCase("one&two&three&four&five&six&seven")
        assertEquals(false, result.isSuccessful)
    }

    @Test
    fun `url param key containing asterisk symbol is unsuccessful`() {
        val result = validateUrlParamKeyUseCase("rule*name")
        assertEquals(false, result.isSuccessful)
    }

    @Test
    fun `url param key containing round brackets is unsuccessful`() {
        val result1 = validateUrlParamKeyUseCase("(thisisonlyonewordbutcontainsabracket")
        assertEquals(false, result1.isSuccessful)

        val result2 = validateUrlParamKeyUseCase("and)this)hasmultipleclosingbrackets)))))))")
        assertEquals(false, result2.isSuccessful)

        val result3 = validateUrlParamKeyUseCase("(")
        assertEquals(false, result3.isSuccessful)

        val result4 = validateUrlParamKeyUseCase(")")
        assertEquals(false, result4.isSuccessful)

        val result5 = validateUrlParamKeyUseCase("()")
        assertEquals(false, result5.isSuccessful)
    }

    @Test
    fun `url param key containing square brackets is unsuccessful`() {
        val result1 = validateUrlParamKeyUseCase("[thisisonlyonewordbutcontainsabracket")
        assertEquals(false, result1.isSuccessful)

        val result2 = validateUrlParamKeyUseCase("and]this]hasmultipleclosingbrackets]]]]]]]")
        assertEquals(false, result2.isSuccessful)

        val result3 = validateUrlParamKeyUseCase("[")
        assertEquals(false, result3.isSuccessful)

        val result4 = validateUrlParamKeyUseCase("]")
        assertEquals(false, result4.isSuccessful)

        val result5 = validateUrlParamKeyUseCase("[]")
        assertEquals(false, result5.isSuccessful)
    }

    @Test
    fun `url param key containing curly braces is unsuccessful`() {
        val result1 = validateUrlParamKeyUseCase("{thisisonlyonewordbutcontainsabracket")
        assertEquals(false, result1.isSuccessful)

        val result2 = validateUrlParamKeyUseCase("and}this}hasmultipleclosingbrackets}}}}}}}")
        assertEquals(false, result2.isSuccessful)

        val result3 = validateUrlParamKeyUseCase("{")
        assertEquals(false, result3.isSuccessful)

        val result4 = validateUrlParamKeyUseCase("}")
        assertEquals(false, result4.isSuccessful)

        val result5 = validateUrlParamKeyUseCase("{}")
        assertEquals(false, result5.isSuccessful)
    }

    @Test
    fun `url param key containing colon is unsuccessful`() {
        val result1 = validateUrlParamKeyUseCase(":")
        assertEquals(false, result1.isSuccessful)

        val result2 = validateUrlParamKeyUseCase("hello:world:this:is:fix:my:links")
        assertEquals(false, result2.isSuccessful)

        val result3 = validateUrlParamKeyUseCase(":testurlparameter")
        assertEquals(false, result3.isSuccessful)
    }

    @Test
    fun `url param key containing pipe character is unsuccessful`() {
        val result1 = validateUrlParamKeyUseCase("|")
        assertEquals(false, result1.isSuccessful)

        val result2 = validateUrlParamKeyUseCase("hello|world|this|is|fix|my|links")
        assertEquals(false, result2.isSuccessful)

        val result3 = validateUrlParamKeyUseCase("|testurlparameter")
        assertEquals(false, result3.isSuccessful)
    }

    @Test
    fun `url param key containing all lowercase letters is successful`() {
        val result1 = validateUrlParamKeyUseCase("probablythemostcommoncasehere")
        assertEquals(true, result1.isSuccessful)

        val result2 = validateUrlParamKeyUseCase("a")
        assertEquals(true, result2.isSuccessful)
    }

    @Test
    fun `url param key containing all digits is successful`() {
        val result1 = validateUrlParamKeyUseCase("01289182787432824972374232423423423243")
        assertEquals(true, result1.isSuccessful)

        val result2 = validateUrlParamKeyUseCase("1")
        assertEquals(true, result2.isSuccessful)
    }

    @Test
    fun `url param key containing all hyphens is successful`() {
        val result1 = validateUrlParamKeyUseCase("---------------------------------")
        assertEquals(true, result1.isSuccessful)

        val result2 = validateUrlParamKeyUseCase("-")
        assertEquals(true, result2.isSuccessful)
    }

    @Test
    fun `url param key containing mixed case characters and hyphens is successful`() {
        val result = validateUrlParamKeyUseCase("OK-so-This-REALLY-should-be-valid")
        assertEquals(true, result.isSuccessful)
    }

    @Test
    fun `url param key containing underscores is successful`() {
        val result1 = validateUrlParamKeyUseCase("my_param_key")
        assertEquals(true, result1.isSuccessful)

        val result2 = validateUrlParamKeyUseCase("___source")
        assertEquals(true, result2.isSuccessful)

        val result3 = validateUrlParamKeyUseCase("_id")
        assertEquals(true, result3.isSuccessful)
    }

    @Test
    fun `url param key containing dots is successful`() {
        val result = validateUrlParamKeyUseCase("application.version.string")
        assertEquals(true, result.isSuccessful)
    }

    @Test
    fun `url param key containing tilde is successful`() {
        val result1 = validateUrlParamKeyUseCase("~campaign")
        assertEquals(true, result1.isSuccessful)

        val result2 = validateUrlParamKeyUseCase("~")
        assertEquals(true, result2.isSuccessful)
    }

    @Test
    fun `url param key containing all allowed character types is successful`() {
        val result = validateUrlParamKeyUseCase("~campaign_id_01-01-2023-Content-Type~application.version_STRING")
        assertEquals(true, result.isSuccessful)
    }
}