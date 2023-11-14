package com.suvanl.fixmylinks.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.suvanl.fixmylinks.domain.mutation.MutationType

@Entity(tableName = "base_rule")
data class BaseRule(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,

    @ColumnInfo(name = "author_id")
    val authorId: String,

    @ColumnInfo(name = "mutation_type")
    val mutationType: MutationType,

    @ColumnInfo(name = "trigger_domain")
    val triggerDomain: String,

    @ColumnInfo(name = "date_modified")
    val dateModified: Long = System.currentTimeMillis() / 1000,

    @ColumnInfo(name = "local_only")
    val isLocalOnly: Boolean,
)