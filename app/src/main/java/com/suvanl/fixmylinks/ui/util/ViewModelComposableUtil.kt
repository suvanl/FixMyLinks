package com.suvanl.fixmylinks.ui.util

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddAllUrlParamsRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddDomainNameRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddSpecificUrlParamsRuleViewModel

/**
 * Returns the [hiltViewModel] composable associated with the given MutationType
 */
@Composable
fun getNewRuleFlowViewModel(mutationType: MutationType): AddRuleViewModel {
    return when (mutationType) {
        MutationType.DOMAIN_NAME -> {
            hiltViewModel<AddDomainNameRuleViewModel>()
        }

        MutationType.URL_PARAMS_ALL -> {
            hiltViewModel<AddAllUrlParamsRuleViewModel>()
        }

        MutationType.URL_PARAMS_SPECIFIC -> {
            hiltViewModel<AddSpecificUrlParamsRuleViewModel>()
        }

        MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL -> {
            hiltViewModel<AddDomainNameRuleViewModel>()
        }

        else -> throw IllegalArgumentException("Invalid/unselectable MutationType")
    }
}
