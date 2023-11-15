package com.suvanl.fixmylinks.ui.util

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.suvanl.fixmylinks.di.AppViewModelProvider
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddAllUrlParamsRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddDomainNameRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddSpecificUrlParamsRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.NewRuleFlowViewModel

/**
 * Returns the viewModel composable associated with the given MutationType
 */
@Composable
fun getNewRuleFlowViewModel(mutationType: MutationType): NewRuleFlowViewModel {
    return when (mutationType) {
        MutationType.DOMAIN_NAME -> {
            viewModel<AddDomainNameRuleViewModel>()
        }

        MutationType.URL_PARAMS_ALL -> {
            viewModel<AddAllUrlParamsRuleViewModel>(
                factory = AppViewModelProvider.Factory
            )
        }

        MutationType.URL_PARAMS_SPECIFIC -> {
            viewModel<AddSpecificUrlParamsRuleViewModel>()
        }

        MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL -> {
            viewModel<AddDomainNameRuleViewModel>()
        }

        else -> throw IllegalArgumentException("Invalid/unselectable MutationType")
    }
}
