package com.suvanl.fixmylinks.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.suvanl.fixmylinks.data.local.db.dao.AllUrlParamsRuleDao
import com.suvanl.fixmylinks.data.local.db.dao.BaseRuleDao
import com.suvanl.fixmylinks.data.local.db.dao.DomainNameAndAllUrlParamsRuleDao
import com.suvanl.fixmylinks.data.local.db.dao.DomainNameRuleDao
import com.suvanl.fixmylinks.data.local.db.entity.AllUrlParamsRule
import com.suvanl.fixmylinks.data.local.db.entity.BaseRule
import com.suvanl.fixmylinks.data.local.db.entity.DomainNameAndAllUrlParamsRule
import com.suvanl.fixmylinks.data.local.db.entity.DomainNameAndSpecificUrlParamsRule
import com.suvanl.fixmylinks.data.local.db.entity.DomainNameRule
import com.suvanl.fixmylinks.data.local.db.entity.SpecificUrlParamsRule

@Database(
    entities = [
        BaseRule::class,
        AllUrlParamsRule::class,
        DomainNameAndAllUrlParamsRule::class,
        DomainNameAndSpecificUrlParamsRule::class,
        DomainNameRule::class,
        SpecificUrlParamsRule::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RuleDatabase : RoomDatabase() {
    abstract fun baseRuleDao(): BaseRuleDao
    abstract fun allUrlParamsRuleDao(): AllUrlParamsRuleDao
    abstract fun domainNameAndALlUrlParamsRuleDao(): DomainNameAndAllUrlParamsRuleDao
    abstract fun domainNameRuleDao(): DomainNameRuleDao

    companion object {
        @Volatile
        private var Instance: RuleDatabase? = null

        fun getDatabase(context: Context): RuleDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, RuleDatabase::class.java, "rule_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        Instance = it
                    }
            }
        }
    }
}