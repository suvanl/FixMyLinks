package com.suvanl.fixmylinks.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.suvanl.fixmylinks.data.local.db.entity.BaseRule
import com.suvanl.fixmylinks.data.local.db.entity.SpecificUrlParamsRule
import kotlinx.coroutines.flow.Flow

@Dao
interface SpecificUrlParamsRuleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: SpecificUrlParamsRule)

    @Update
    suspend fun update(rule: SpecificUrlParamsRule)

    @Delete
    suspend fun delete(rule: SpecificUrlParamsRule)

    @Query(
        "SELECT * FROM specific_url_params_rule AS rule " +
        "JOIN base_rule ON rule.base_rule_id = base_rule.id"
    )
    fun getAll(): Flow<Map<BaseRule, SpecificUrlParamsRule>>
}