package com.suvanl.fixmylinks.di

import com.suvanl.fixmylinks.data.repository.RulesRepository

interface AppContainer {
    val rulesRepository: RulesRepository
}