package com.suvanl.fixmylinks.viewmodel.newruleflow

import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddSpecificUrlParamsRuleViewModel(
    private val rulesRepository: RulesRepository
) : AddRuleViewModel() {

    private val _ruleName = MutableStateFlow("")
    val ruleName = _ruleName.asStateFlow()

    private val _domainName = MutableStateFlow("")
    val domainName = _domainName.asStateFlow()

    private val _removableParams = MutableStateFlow(emptyList<String>())
    val removableParams = _removableParams.asStateFlow()

    fun setRuleName(ruleName: String) {
        _ruleName.value = ruleName
    }

    fun setDomainName(domainName: String) {
        _domainName.value = domainName
    }

    fun addParam(paramName: String) {
        _removableParams.value += paramName
    }

    fun removeParam(index: Int) {
        val mutable = _removableParams.value.toMutableList()
        mutable.removeAt(index)

        _removableParams.value = mutable.toList()
    }

    override suspend fun saveRule() {
        rulesRepository.saveRule(
            SpecificUrlParamsMutationModel(
                name = _ruleName.value,
                triggerDomain = _domainName.value,
                isLocalOnly = true,
                mutationInfo = SpecificUrlParamsMutationInfo(
                    removableParams = _removableParams.value
                )
            )
        )
    }

    override fun validateData(): Boolean {
        TODO("Not yet implemented")
    }
}