package com.suvanl.fixmylinks.di

import com.suvanl.fixmylinks.data.repository.CustomRulesRepository
import com.suvanl.fixmylinks.data.repository.RulesRepository
import com.suvanl.fixmylinks.domain.validation.Validator
import com.suvanl.fixmylinks.ui.util.DomainNameValidator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Provides dependencies scoped to the ViewModel lifecycle
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindDomainNameValidator(
        domainNameValidator: DomainNameValidator
    ): Validator

    @Binds
    @ViewModelScoped
    abstract fun bindRulesRepository(
        customRulesRepository: CustomRulesRepository
    ): RulesRepository
}