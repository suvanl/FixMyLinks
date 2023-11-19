package com.suvanl.fixmylinks.di

import android.content.Context
import com.suvanl.fixmylinks.data.local.db.RuleDatabase
import com.suvanl.fixmylinks.data.repository.RulesRepository

class AppDataContainer(private val context: Context) : AppContainer {
    override val rulesRepository: RulesRepository by lazy {
        RulesRepository(RuleDatabase.getDatabase(context = context))
    }
}