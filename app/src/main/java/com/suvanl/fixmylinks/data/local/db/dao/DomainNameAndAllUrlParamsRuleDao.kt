package com.suvanl.fixmylinks.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.suvanl.fixmylinks.data.local.db.entity.AllUrlParamsRule
import com.suvanl.fixmylinks.data.local.db.entity.BaseRule
import com.suvanl.fixmylinks.data.local.db.entity.DomainNameAndAllUrlParamsRule
import kotlinx.coroutines.flow.Flow

@Dao
interface DomainNameAndAllUrlParamsRuleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: DomainNameAndAllUrlParamsRule)

    @Update
    suspend fun update(rule: DomainNameAndAllUrlParamsRule)

    @Delete
    suspend fun delete(rule: DomainNameAndAllUrlParamsRule)

    @Query(
        "SELECT * FROM domain_name_and_all_url_params_rule AS rule " +
        "JOIN base_rule ON rule.base_rule_id = base_rule.id"
    )
    fun getAll(): Flow<Map<BaseRule, DomainNameAndAllUrlParamsRule>>

    @Query(
        "SELECT * FROM domain_name_and_all_url_params_rule AS rule " +
        "JOIN base_rule ON rule.base_rule_id = base_rule.id " +
        "WHERE rule.base_rule_id = :baseRuleId"
    )
    fun getByBaseRuleId(baseRuleId: Long): Flow<Map<BaseRule, AllUrlParamsRule>>
}