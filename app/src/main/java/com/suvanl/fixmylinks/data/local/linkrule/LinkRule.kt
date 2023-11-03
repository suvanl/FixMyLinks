package com.suvanl.fixmylinks.data.local.linkrule

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "link_rules")
data class LinkRule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)