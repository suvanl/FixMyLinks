package com.suvanl.fixmylinks.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "domain_name_and_specific_url_params_rule",
    foreignKeys = [
        ForeignKey(
            entity = BaseRule::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("base_rule_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DomainNameAndSpecificUrlParamsRule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "base_rule_id", index = true)
    val baseRuleId: Int,

    @ColumnInfo(name = "initial_domain_name")
    val initialDomainName: String,

    @ColumnInfo(name = "target_domain_name")
    val targetDomainName: String,

    @ColumnInfo(name = "removable_params")
    val removableParams: List<String>,
)
