package com.suvanl.fixmylinks.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.suvanl.fixmylinks.data.local.db.entity.BaseRule
import com.suvanl.fixmylinks.data.local.db.entity.DomainNameRule
import kotlinx.coroutines.flow.Flow

@Dao
interface DomainNameRuleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: DomainNameRule)

    @Update
    suspend fun update(rule: DomainNameRule)

    @Delete
    suspend fun delete(rule: DomainNameRule)

    @Query(
        "SELECT * FROM domain_name_rule AS rule " +
        "JOIN base_rule ON rule.base_rule_id = base_rule.id"
    )
    fun getAll(): Flow<Map<BaseRule, DomainNameRule>>
}