package com.suvanl.fixmylinks.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.suvanl.fixmylinks.data.local.db.entity.BaseRule

@Dao
interface BaseRuleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: BaseRule): Long

    @Update
    suspend fun update(rule: BaseRule)

    @Delete
    suspend fun delete(rule: BaseRule)

    /**
     * Deletes **EVERYTHING** from the local database using the SQLite truncate optimisation.
     * Use with caution.
     */
    @Query("DELETE FROM base_rule")
    suspend fun deleteAll()
}