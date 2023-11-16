package com.suvanl.fixmylinks.di

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.suvanl.fixmylinks.FmlApplication
import com.suvanl.fixmylinks.domain.mutation.MutateUriUseCase
import com.suvanl.fixmylinks.viewmodel.RulesViewModel
import com.suvanl.fixmylinks.viewmodel.ShareViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddAllUrlParamsRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddDomainNameRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddSpecificUrlParamsRuleViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            RulesViewModel(
                rulesRepository = application().container.rulesRepository
            )
        }

        initializer {
            AddAllUrlParamsRuleViewModel(
                rulesRepository = application().container.rulesRepository
            )
        }

        initializer {
            AddDomainNameRuleViewModel(
                rulesRepository = application().container.rulesRepository
            )
        }

        initializer {
            AddSpecificUrlParamsRuleViewModel(
                rulesRepository = application().container.rulesRepository
            )
        }

        // ShareViewModel
        initializer {
            ShareViewModel(
                mutateUriUseCase = MutateUriUseCase(),
                rulesRepository = application().container.rulesRepository
            )
        }
    }
}

fun CreationExtras.application(): FmlApplication = (this[APPLICATION_KEY] as FmlApplication)
