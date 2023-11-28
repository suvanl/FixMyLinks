package com.suvanl.fixmylinks.viewmodel

import com.suvanl.fixmylinks.data.repository.FakeRulesRepository
import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.domain.mutation.model.AllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndSpecificUrlParamsMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndSpecificUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationModel
import com.suvanl.fixmylinks.ui.screens.RulesScreenUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RulesViewModelTest {

    private lateinit var viewModel: RulesViewModel
    private lateinit var repository: RulesRepository

    /**
     * Populate the "database" (a [List], since this test isn't testing DB functionality) with
     * fake/sample rules.
     */
    private suspend fun saveAllFakeCustomRules() {
        fakeCustomRules.forEach { repository.saveRule(it) }
    }

    @Before
    fun setup() {
        repository = FakeRulesRepository()
        viewModel = RulesViewModel(repository)
    }

    @Test
    fun `all custom rules are returned as observable RulesScreenUiState`() = runBlocking {
        saveAllFakeCustomRules()

        val rulesScreenUiState: Flow<RulesScreenUiState> =
            repository.getAllRules()
                .map { RulesScreenUiState(rules = it) }

        val actualState = rulesScreenUiState.first()
        val expectedState = RulesScreenUiState(rules = fakeCustomRules)

        assertEquals(expectedState, actualState)
    }

    @Test
    fun `deleting a single rule when only one rule is saved results in an empty list for RulesScreenUiState#rules`() =
        runBlocking {
            // Save a random single rule
            val rule = fakeCustomRules[(fakeCustomRules.indices).random()]
            repository.saveRule(rule)

            val rulesScreenUiState: Flow<RulesScreenUiState> =
                repository.getAllRules()
                    .map { RulesScreenUiState(rules = it) }

            // Delete single rule
            repository.deleteByBaseRuleId(rule.baseRuleId)

            val actualState = rulesScreenUiState.first()
            val expectedState = RulesScreenUiState(rules = emptyList())

            assertEquals(expectedState, actualState)
        }

    @Test
    fun `deleting a single rule when multiple rules are saved results in an empty list for RulesScreenUiState#rules`() =
        runBlocking {
            saveAllFakeCustomRules()

            val rulesScreenUiState: Flow<RulesScreenUiState> =
                repository.getAllRules()
                    .map { RulesScreenUiState(rules = it) }

            // Get a random rule
            val randomRule = fakeCustomRules[(fakeCustomRules.indices).random()]

            // Remove the random rule
            repository.deleteByBaseRuleId(randomRule.baseRuleId)
            val updatedData = fakeCustomRules.filter { it != randomRule }

            val actualState = rulesScreenUiState.first()
            val expectedState = RulesScreenUiState(rules = updatedData)

            assertEquals(expectedState, actualState)
        }

    @Test
    fun `deleting all rules when saved rules exist in the results in an empty list for RulesScreenUiState#rules`() =
        runBlocking {
            saveAllFakeCustomRules()

            val rulesScreenUiState: Flow<RulesScreenUiState> =
                repository.getAllRules()
                    .map { RulesScreenUiState(rules = it) }

            // Delete all rules
            repository.deleteAllRules()

            val actualState = rulesScreenUiState.first()
            val expectedState = RulesScreenUiState(rules = emptyList())

            assertEquals(expectedState, actualState)
        }

    companion object {
        private val fakeCustomRules = listOf(
            DomainNameMutationModel(
                name = "test rule 1",
                triggerDomain = "google.com",
                isLocalOnly = true,
                mutationInfo = DomainNameMutationInfo(
                    initialDomain = "google.com",
                    targetDomain = "google.co.uk",
                ),
                baseRuleId = 1,
            ),
            AllUrlParamsMutationModel(
                name = "My rule",
                triggerDomain = "instagram.com",
                isLocalOnly = true,
                baseRuleId = 2,
            ),
            AllUrlParamsMutationModel(
                name = "Spotify - remove all URL params",
                triggerDomain = "spotify.com",
                isLocalOnly = true,
                baseRuleId = 3,
            ),
            SpecificUrlParamsMutationModel(
                name = "YouTube - remove playlist association",
                triggerDomain = "youtube.com",
                isLocalOnly = true,
                mutationInfo = SpecificUrlParamsMutationInfo(
                    removableParams = listOf("list")
                ),
                baseRuleId = 4,
            ),
            DomainNameAndSpecificUrlParamsMutationModel(
                name = "x to twitter",
                triggerDomain = "x.com",
                isLocalOnly = true,
                mutationInfo = DomainNameAndSpecificUrlParamsMutationInfo(
                    initialDomainName = "x.com",
                    targetDomainName = "twitter.com",
                    removableParams = listOf("s")
                ),
                baseRuleId = 5,
            ),
        )
    }
}