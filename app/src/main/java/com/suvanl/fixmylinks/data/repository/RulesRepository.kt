package com.suvanl.fixmylinks.data.repository

import androidx.room.withTransaction
import com.suvanl.fixmylinks.data.local.db.RuleDatabase
import com.suvanl.fixmylinks.data.local.db.entity.AllUrlParamsRule
import com.suvanl.fixmylinks.data.local.db.entity.BaseRule
import com.suvanl.fixmylinks.data.local.db.entity.DomainNameAndAllUrlParamsRule
import com.suvanl.fixmylinks.data.local.db.entity.DomainNameAndSpecificUrlParamsRule
import com.suvanl.fixmylinks.data.local.db.entity.DomainNameRule
import com.suvanl.fixmylinks.domain.mutation.model.AllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndSpecificUrlParamsMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndSpecificUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.toDatabaseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class RulesRepository(private val localDatabase: RuleDatabase) {
    /**
     * Inserts a new rule into the database. The rule will exist in the DB as the entity equivalent
     * of the type of the given domain model ([rule]).
     */
    suspend fun insertRule(rule: BaseMutationModel) = localDatabase.apply {
        withTransaction {
            val baseRuleId = baseRuleDao().insert(rule.toDatabaseEntity())

            when (rule) {
                is AllUrlParamsMutationModel -> {
                    allUrlParamsRuleDao().insert(rule.toDatabaseEntity(baseRuleId))
                }

                is DomainNameMutationModel -> {
                    domainNameRuleDao().insert(rule.toDatabaseEntity(baseRuleId))
                }

                is DomainNameAndSpecificUrlParamsMutationModel -> TODO()
                is DomainNameAndAllUrlParamsMutationModel -> {
                    domainNameAndALlUrlParamsRuleDao().insert(rule.toDatabaseEntity(baseRuleId))
                }

                is SpecificUrlParamsMutationModel -> TODO()

                else -> {
                    throw IllegalArgumentException(
                        "rule is not of a supported type that extends BaseMutationModel"
                    )
                }
            }
        }
    }

    /**
     * Deletes ALL custom rules.
     */
    suspend fun deleteAllRules() = localDatabase.baseRuleDao().deleteAll()

    /**
     * Gets all rules (of all types) from the local database and exposes them as a [Flow].
     */
    fun getAllRules(): Flow<List<BaseMutationModel>> {
        val allUrlParamsRules = localDatabase.allUrlParamsRuleDao().getAll().toDomainModelListFlow()
        val domainNameRules = localDatabase.domainNameRuleDao().getAll().toDomainModelListFlow()
        val domainNameAndAllUrlParamsRules =
            localDatabase.domainNameAndALlUrlParamsRuleDao().getAll().toDomainModelListFlow()

        val combinedFlow: Flow<List<BaseMutationModel>> =
            combine(
                allUrlParamsRules,
                domainNameRules,
                domainNameAndAllUrlParamsRules
            ) { combinedArray ->
                // flatten the Array<List<BaseMutationModel>> to be a List<BaseMutationModel>
                combinedArray.flatMap { it }
            }

        return combinedFlow
    }

    /**
     * Converts this multimap Flow returned by a DAO function to a list of domain model objects
     * implementing [BaseMutationModel].
     *
     * **See also**: [Multimap return types](https://d.android.com/training/data-storage/room/relationships#multimap)
     *
     * @param TSource The type of the Map's value, where the Map is a multimap returned by a
     *  DAO query function.
     *  @return A list of domain models ([BaseMutationModel]-derived objects).
     */
    private fun <TSource> Flow<Map<BaseRule, TSource>>.toDomainModelListFlow(): Flow<List<BaseMutationModel>> {
        return this.map {
            it.map { (baseRule, genericRule) ->
                when (genericRule) {
                    is AllUrlParamsRule -> {
                        AllUrlParamsMutationModel(
                            name = baseRule.title,
                            mutationType = baseRule.mutationType,
                            triggerDomain = baseRule.triggerDomain,
                            dateModifiedTimestamp = baseRule.dateModified,
                            isLocalOnly = baseRule.isLocalOnly,
                        )
                    }

                    is DomainNameAndAllUrlParamsRule -> {
                        DomainNameAndAllUrlParamsMutationModel(
                            name = baseRule.title,
                            mutationType = baseRule.mutationType,
                            triggerDomain = baseRule.triggerDomain,
                            dateModifiedTimestamp = baseRule.dateModified,
                            isLocalOnly = baseRule.isLocalOnly,
                            mutationInfo = DomainNameMutationInfo(
                                initialDomain = genericRule.initialDomainName,
                                targetDomain = genericRule.targetDomainName
                            )
                        )
                    }

                    is DomainNameAndSpecificUrlParamsRule -> {
                        DomainNameAndSpecificUrlParamsMutationModel(
                            name = baseRule.title,
                            mutationType = baseRule.mutationType,
                            triggerDomain = baseRule.triggerDomain,
                            dateModifiedTimestamp = baseRule.dateModified,
                            isLocalOnly = baseRule.isLocalOnly,
                            mutationInfo = DomainNameAndSpecificUrlParamsMutationInfo(
                                initialDomainName = genericRule.initialDomainName,
                                targetDomainName = genericRule.targetDomainName,
                                removableParams = genericRule.removableParams
                            )
                        )
                    }

                    is DomainNameRule -> {
                        DomainNameMutationModel(
                            name = baseRule.title,
                            mutationType = baseRule.mutationType,
                            triggerDomain = baseRule.triggerDomain,
                            dateModifiedTimestamp = baseRule.dateModified,
                            isLocalOnly = baseRule.isLocalOnly,
                            mutationInfo = DomainNameMutationInfo(
                                initialDomain = genericRule.initialDomainName,
                                targetDomain = genericRule.targetDomainName
                            )
                        )
                    }

                    is BaseRule -> {
                        throw IllegalArgumentException("Type of multimap value should not be BaseRule")
                    }

                    else -> throw IllegalArgumentException("Multimap value has unexpected type")
                }
            }
        }
    }
}