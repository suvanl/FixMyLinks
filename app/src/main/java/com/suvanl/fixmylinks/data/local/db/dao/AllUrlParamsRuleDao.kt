package com.suvanl.fixmylinks.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.suvanl.fixmylinks.data.local.db.entity.AllUrlParamsRule
import com.suvanl.fixmylinks.data.local.db.entity.BaseRule
import kotlinx.coroutines.flow.Flow

@Dao
interface AllUrlParamsRuleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: AllUrlParamsRule)

    @Update
    suspend fun update(rule: AllUrlParamsRule)

    @Delete
    suspend fun delete(rule: AllUrlParamsRule)

    @Query(
        "SELECT * FROM all_url_params_rule AS rule " +
        "JOIN base_rule ON rule.base_rule_id = base_rule.id " +
        "WHERE rule.base_rule_id = :id"
    )
    fun getRule(id: Long): Flow<Map<BaseRule, AllUrlParamsRule>>

    @Query(
        "SELECT * FROM all_url_params_rule AS rule " +
        "JOIN base_rule ON rule.base_rule_id = base_rule.id"
    )
    fun getAll(): Flow<Map<BaseRule, AllUrlParamsRule>>
}