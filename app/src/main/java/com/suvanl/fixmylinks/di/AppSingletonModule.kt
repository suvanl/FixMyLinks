package com.suvanl.fixmylinks.di

import android.content.Context
import com.suvanl.fixmylinks.data.local.db.RuleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides singleton dependencies that live as long as the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppSingletonModule {

    @Provides
    @Singleton
    fun provideRuleDatabase(@ApplicationContext context: Context): RuleDatabase {
        return RuleDatabase.getDatabase(context = context)
    }
}