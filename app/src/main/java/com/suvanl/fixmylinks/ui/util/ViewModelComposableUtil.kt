package com.suvanl.fixmylinks.ui.util

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.suvanl.fixmylinks.di.AppViewModelProvider
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddAllUrlParamsRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddDomainNameRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddSpecificUrlParamsRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddRuleViewModel

/**
 * Returns the viewModel composable associated with the given MutationType
 */
@Composable
fun getNewRuleFlowViewModel(mutationType: MutationType): AddRuleViewModel {
    return when (mutationType) {
        MutationType.DOMAIN_NAME -> {
            hiltViewModel<AddDomainNameRuleViewModel>()
        }

        MutationType.URL_PARAMS_ALL -> {
            viewModel<AddAllUrlParamsRuleViewModel>(
                factory = AppViewModelProvider.Factory
            )
        }

        MutationType.URL_PARAMS_SPECIFIC -> {
            viewModel<AddSpecificUrlParamsRuleViewModel>(
                factory = AppViewModelProvider.Factory
            )
        }

        MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL -> {
            viewModel<AddDomainNameRuleViewModel>(
                factory = AppViewModelProvider.Factory
            )
        }

        else -> throw IllegalArgumentException("Invalid/unselectable MutationType")
    }
}
