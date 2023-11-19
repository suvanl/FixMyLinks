package com.suvanl.fixmylinks.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "specific_url_params_rule",
    foreignKeys = [
        ForeignKey(
            entity = BaseRule::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("base_rule_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SpecificUrlParamsRule(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "base_rule_id", index = true)
    val baseRuleId: Long,

    @ColumnInfo(name = "removable_params")
    val removableParams: List<String>,
)