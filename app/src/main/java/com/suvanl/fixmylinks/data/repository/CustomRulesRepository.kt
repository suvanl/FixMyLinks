package com.suvanl.fixmylinks.data.repository

import androidx.room.withTransaction
import com.suvanl.fixmylinks.data.local.db.RuleDatabase
import com.suvanl.fixmylinks.data.local.db.entity.AllUrlParamsRule
import com.suvanl.fixmylinks.data.local.db.entity.BaseRule
import com.suvanl.fixmylinks.data.local.db.entity.DomainNameAndAllUrlParamsRule
import com.suvanl.fixmylinks.data.local.db.entity.DomainNameAndSpecificUrlParamsRule
import com.suvanl.fixmylinks.data.local.db.entity.DomainNameRule
import com.suvanl.fixmylinks.data.local.db.entity.SpecificUrlParamsRule
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.domain.mutation.model.AllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndSpecificUrlParamsMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndSpecificUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.toDatabaseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CustomRulesRepository @Inject constructor(
    private val localDatabase: RuleDatabase
) : RulesRepository {
    /**
     * Save a rule locally (and optionally remotely based on user preferences).
     */
    override suspend fun saveRule(rule: BaseMutationModel) {
        insertRule(rule)

        // then perform logic for saving a rule remotely (if requested by user)
    }

    /**
     * Delete the rule with the given [BaseRule].`id`, which will also delete the specific rule
     * with the baseRuleId matching the given value.
     */
    override suspend fun deleteByBaseRuleId(baseRuleId: Long) {
        localDatabase.baseRuleDao().deleteById(baseRuleId)
        // then perform logic to delete the rule remotely (if it is indeed backed up remotely)
    }

    /**
     * Deletes ALL custom rules.
     */
    override suspend fun deleteAllRules() = localDatabase.baseRuleDao().deleteAll()

    /**
     * Gets all rules (of all types) from the local database and exposes them as a [Flow].
     */
    override fun getAllRules(): Flow<List<BaseMutationModel>> {
        val allUrlParamsRules = localDatabase.allUrlParamsRuleDao().getAll().toDomainModelListFlow()
        val domainNameRules = localDatabase.domainNameRuleDao().getAll().toDomainModelListFlow()
        val domainNameAndAllUrlParamsRules =
            localDatabase.domainNameAndAllUrlParamsRuleDao().getAll().toDomainModelListFlow()
        val domainNameAndSpecificUrlParamsRules =
            localDatabase.domainNameAndSpecificUrlParamsRuleDao().getAll().toDomainModelListFlow()
        val specificUrlParamsRules =
            localDatabase.specificUrlParamsRuleDao().getAll().toDomainModelListFlow()

        val combinedFlow: Flow<List<BaseMutationModel>> =
            combine(
                allUrlParamsRules,
                domainNameRules,
                domainNameAndAllUrlParamsRules,
                domainNameAndSpecificUrlParamsRules,
                specificUrlParamsRules
            ) { combinedArray ->
                // flatten the Array<List<BaseMutationModel>> to a List<BaseMutationModel>
                combinedArray.flatMap { it }
            }

        return combinedFlow
    }

    /**
     * Fetches a rule by its base rule ID.
     * @param baseRuleId The rule's base rule ID.
     * @param ruleType The [MutationType] of the base rule.
     * @return Flow that emits a domain model representing a rule.
     */
    override fun getRuleByBaseId(
        baseRuleId: Long,
        ruleType: MutationType
    ): Flow<BaseMutationModel> {
        localDatabase.apply {
            return when (ruleType) {
                MutationType.URL_PARAMS_ALL -> {
                    allUrlParamsRuleDao().getByBaseRuleId(baseRuleId)
                        .toDomainModelFlow()
                }

                MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL -> {
                    domainNameAndAllUrlParamsRuleDao()
                        .getByBaseRuleId(baseRuleId)
                        .toDomainModelFlow()
                }

                MutationType.DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC -> {
                    domainNameAndSpecificUrlParamsRuleDao()
                        .getByBaseRuleId(baseRuleId)
                        .toDomainModelFlow()
                }

                MutationType.DOMAIN_NAME -> {
                    domainNameRuleDao()
                        .getByBaseRuleId(baseRuleId)
                        .toDomainModelFlow()
                }

                MutationType.URL_PARAMS_SPECIFIC -> {
                    specificUrlParamsRuleDao()
                        .getByBaseRuleId(baseRuleId)
                        .toDomainModelFlow()
                }

                else -> throw IllegalArgumentException(
                    "ruleType has unexpected type (${ruleType::class.simpleName})"
                )
            }
        }
    }

    /**
     * Inserts a new rule into the database. The rule will exist in the DB as the entity equivalent
     * of the type of the given domain model ([rule]).
     */
    private suspend fun insertRule(rule: BaseMutationModel) = localDatabase.apply {
        withTransaction {
            val baseRuleId = baseRuleDao().insert(rule.toDatabaseEntity())

            when (rule) {
                is AllUrlParamsMutationModel -> {
                    allUrlParamsRuleDao().insert(rule.toDatabaseEntity(baseRuleId))
                }

                is DomainNameMutationModel -> {
                    domainNameRuleDao().insert(rule.toDatabaseEntity(baseRuleId))
                }

                is DomainNameAndSpecificUrlParamsMutationModel -> {
                    domainNameAndSpecificUrlParamsRuleDao().insert(rule.toDatabaseEntity(baseRuleId))
                }

                is DomainNameAndAllUrlParamsMutationModel -> {
                    domainNameAndAllUrlParamsRuleDao().insert(rule.toDatabaseEntity(baseRuleId))
                }

                is SpecificUrlParamsMutationModel -> {
                    specificUrlParamsRuleDao().insert(rule.toDatabaseEntity(baseRuleId))
                }

                else -> {
                    throw IllegalArgumentException(
                        "rule is not of a supported type that extends BaseMutationModel"
                    )
                }
            }
        }
    }

    /**
     * Converts this multimap Flow returned by a DAO function to a list of domain model objects
     * implementing [BaseMutationModel].
     *
     * **See also**: [Multimap return types](https://d.android.com/training/data-storage/room/relationships#multimap)
     *
     * @param TValue The type of the Map's value, where the Map is a multimap returned by a
     *  DAO query function.
     * @return A list of domain models ([BaseMutationModel]-derived objects).
     */
    private fun <TValue> Flow<Map<BaseRule, TValue>>.toDomainModelListFlow(): Flow<List<BaseMutationModel>> {
        return this.map {
            it.map { (baseRule, genericRule) ->
                mapEntityToDomainModel(baseRule, genericRule)
            }
        }
    }

    /**
     * Converts this multimap Flow returned by a DAO function to a domain model object
     * implementing [BaseMutationModel].
     *
     * @param TValue The type of the Map's value, where the Map is a multimap returned by a
     *  DAO query function.
     * @return A domain model ([BaseMutationModel]-derived objects).
     */
    private fun <TValue> Flow<Map<BaseRule, TValue>>.toDomainModelFlow(): Flow<BaseMutationModel> {
        return this.map { multimap ->
            val (baseRule, genericRule) = multimap.entries.first().toPair()
            mapEntityToDomainModel(baseRule, genericRule)
        }
    }

    /**
     * Returns a domain model instance based on the type of [T].
     */
    private fun <T> mapEntityToDomainModel(baseRule: BaseRule, genericRule: T): BaseMutationModel {
        return when (genericRule) {
            is AllUrlParamsRule -> {
                AllUrlParamsMutationModel(
                    baseRuleId = baseRule.id,
                    name = baseRule.title,
                    triggerDomain = baseRule.triggerDomain,
                    dateModifiedTimestamp = baseRule.dateModified,
                    isLocalOnly = baseRule.isLocalOnly,
                )
            }

            is DomainNameAndAllUrlParamsRule -> {
                DomainNameAndAllUrlParamsMutationModel(
                    baseRuleId = baseRule.id,
                    name = baseRule.title,
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
                    baseRuleId = baseRule.id,
                    name = baseRule.title,
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
                    baseRuleId = baseRule.id,
                    name = baseRule.title,
                    triggerDomain = baseRule.triggerDomain,
                    dateModifiedTimestamp = baseRule.dateModified,
                    isLocalOnly = baseRule.isLocalOnly,
                    mutationInfo = DomainNameMutationInfo(
                        initialDomain = genericRule.initialDomainName,
                        targetDomain = genericRule.targetDomainName
                    )
                )
            }

            is SpecificUrlParamsRule -> {
                SpecificUrlParamsMutationModel(
                    baseRuleId = baseRule.id,
                    name = baseRule.title,
                    triggerDomain = baseRule.triggerDomain,
                    dateModifiedTimestamp = baseRule.dateModified,
                    isLocalOnly = baseRule.isLocalOnly,
                    mutationInfo = SpecificUrlParamsMutationInfo(
                        removableParams = genericRule.removableParams
                    )
                )
            }

            is BaseRule -> {
                throw IllegalArgumentException(
                    "Type of multimap value should not be BaseRule"
                )
            }

            else -> throw IllegalArgumentException(
                "Multimap value has unexpected type (${genericRule!!::class.simpleName})"
            )
        }
    }
}