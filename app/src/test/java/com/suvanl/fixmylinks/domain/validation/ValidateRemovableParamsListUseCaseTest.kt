package com.suvanl.fixmylinks.domain.validation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidateRemovableParamsListUseCaseTest {

    private lateinit var validateRemovableParamsListUseCase: ValidateRemovableParamsListUseCase

    @Before
    fun setup() {
        validateRemovableParamsListUseCase = ValidateRemovableParamsListUseCase()
    }

    @Test
    fun `empty parameter list fails validation (using 'listOf()')`() {
        val result = validateRemovableParamsListUseCase(listOf())
        assertFalse(result.isSuccessful)
    }

    @Test
    fun `empty parameter list fails validation (using 'emptyList()')`() {
        val result = validateRemovableParamsListUseCase(emptyList())
        assertFalse(result.isSuccessful)
    }

    @Test
    fun `list containing single parameter passes validation`() {
        val result = validateRemovableParamsListUseCase(listOf("singleparam"))
        assertTrue(result.isSuccessful)
    }

    @Test
    fun `list containing multiple parameters passes validation`() {
        val result = validateRemovableParamsListUseCase(
            listOf(
                "hello",
                "there",
                "these",
                "are",
                "some",
                "params"
            )
        )
        assertTrue(result.isSuccessful)
    }

    @Test
    fun `list containing a large number of parameters passes validation`() {
        val result = validateRemovableParamsListUseCase(
            listOf(
                "id",
                "name",
                "page",
                "category",
                "search",
                "sort",
                "filter",
                "startDate",
                "endDate",
                "lang",
                "utm_source",
                "utm_medium",
                "utm_campaign",
                "referrer",
                "access_token",
                "client_id",
                "session_id",
                "return_url",
                "locale",
                "format",
                "view",
                "debug",
                "theme",
                "callback",
                "apikey",
                "latitude",
                "longitude",
                "zoom",
                "width",
                "height",
                "autoplay",
                "loop",
                "show_title",
                "show_author",
                "show_date",
                "show_thumbnail",
                "color",
                "size",
                "style",
                "variant",
                "download",
                "upload",
                "subscribe",
                "unsubscribe",
                "share",
                "comment",
                "like",
                "dislike",
                "rate",
                "tags",
                "user",
                "password",
                "email",
                "phone",
                "address",
                "city",
                "state",
                "country",
                "zip",
                "param0",
                "param1",
                "param2",
                "param3",
                "param4",
                "param5",
                "param6",
                "param7",
                "param8",
                "param9",
                "param10",
            )
        )
        assertTrue(result.isSuccessful)
    }
}