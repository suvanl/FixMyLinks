package com.suvanl.fixmylinks.viewmodel.newruleflow

import com.suvanl.fixmylinks.domain.mutation.MutationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SelectRuleTypeViewModelTest {

    private lateinit var viewModel: SelectRuleTypeViewModel

    private val mutationType = MutableStateFlow(MutationType.DOMAIN_NAME)

    @Before
    fun setup() {
        viewModel = SelectRuleTypeViewModel()
    }

    @Test
    fun `setting selected rule type to DOMAIN_NAME changes nothing since it is the default value`() {
        mutationType.value = MutationType.DOMAIN_NAME

        runBlocking {
            val selected = mutationType.first()
            assertEquals(selected, MutationType.DOMAIN_NAME)
        }
    }

    @Test
    fun `setting selected rule type to a value other than the default successfully updates the StateFlow emission(s)`() {
        mutationType.value = MutationType.DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC

        runBlocking {
            val selected = mutationType.first()
            assertEquals(selected, MutationType.DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC)
        }
    }
}