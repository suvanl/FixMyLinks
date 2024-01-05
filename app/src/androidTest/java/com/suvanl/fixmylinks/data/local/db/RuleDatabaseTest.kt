package com.suvanl.fixmylinks.data.local.db

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.suvanl.fixmylinks.data.local.db.dao.AllUrlParamsRuleDao
import com.suvanl.fixmylinks.data.local.db.dao.BaseRuleDao
import com.suvanl.fixmylinks.data.local.db.dao.DomainNameAndAllUrlParamsRuleDao
import com.suvanl.fixmylinks.data.local.db.dao.DomainNameAndSpecificUrlParamsRuleDao
import com.suvanl.fixmylinks.data.local.db.dao.DomainNameRuleDao
import com.suvanl.fixmylinks.data.local.db.dao.SpecificUrlParamsRuleDao
import com.suvanl.fixmylinks.data.local.db.entity.AllUrlParamsRule
import com.suvanl.fixmylinks.data.local.db.entity.BaseRule
import com.suvanl.fixmylinks.data.local.db.entity.DomainNameAndAllUrlParamsRule
import com.suvanl.fixmylinks.data.local.db.entity.DomainNameAndSpecificUrlParamsRule
import com.suvanl.fixmylinks.data.local.db.entity.DomainNameRule
import com.suvanl.fixmylinks.data.local.db.entity.SpecificUrlParamsRule
import com.suvanl.fixmylinks.domain.mutation.MutationType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RuleDatabaseTest {

    private lateinit var ruleDatabase: RuleDatabase

    private lateinit var baseRuleDao: BaseRuleDao

    private lateinit var allUrlParamsRuleDao: AllUrlParamsRuleDao
    private lateinit var domainNameAndAllUrlParamsRuleDao: DomainNameAndAllUrlParamsRuleDao
    private lateinit var domainNameAndSpecificUrlParamsRuleDao: DomainNameAndSpecificUrlParamsRuleDao
    private lateinit var domainNameRuleDao: DomainNameRuleDao
    private lateinit var specificUrlParamsRuleDao: SpecificUrlParamsRuleDao

    /**
     * Insert [rule1_allUrlParams]
     */
    private suspend fun insertRule1() {
        val (baseRule, rule) = rule1_allUrlParams
        ruleDatabase.withTransaction {
            baseRuleDao.insert(baseRule)
            allUrlParamsRuleDao.insert(rule)
        }
    }

    /**
     * Insert [rule2_allUrlParams]
     */
    private suspend fun insertRule2() {
        val (baseRule, rule) = rule2_allUrlParams
        ruleDatabase.withTransaction {
            baseRuleDao.insert(baseRule)
            allUrlParamsRuleDao.insert(rule)
        }
    }

    /**
     * Insert [rule3_domainName]
     */
    private suspend fun insertRule3() {
        val (baseRule, rule) = rule3_domainName
        ruleDatabase.withTransaction {
            baseRuleDao.insert(baseRule)
            domainNameRuleDao.insert(rule)
        }
    }

    /**
     * Insert [rule4_allUrlParams]
     */
    private suspend fun insertRule4() {
        val (baseRule, rule) = rule4_allUrlParams
        ruleDatabase.withTransaction {
            baseRuleDao.insert(baseRule)
            allUrlParamsRuleDao.insert(rule)
        }
    }

    /**
     * Insert [rule5_domainNameAndAllUrlParams]
     */
    private suspend fun insertRule5() {
        val (baseRule, rule) = rule5_domainNameAndAllUrlParams
        ruleDatabase.withTransaction {
            baseRuleDao.insert(baseRule)
            domainNameAndAllUrlParamsRuleDao.insert(rule)
        }
    }

    /**
     * Insert [rule6_domainNameAndSpecificUrlParams]
     */
    private suspend fun insertRule6() {
        val (baseRule, rule) = rule6_domainNameAndSpecificUrlParams
        ruleDatabase.withTransaction {
            baseRuleDao.insert(baseRule)
            domainNameAndSpecificUrlParamsRuleDao.insert(rule)
        }
    }

    /**
     * Insert [rule7_specificUrlParams]
     */
    private suspend fun insertRule7() {
        val (baseRule, rule) = rule7_specificUrlParams
        ruleDatabase.withTransaction {
            baseRuleDao.insert(baseRule)
            specificUrlParamsRuleDao.insert(rule)
        }
    }

    private suspend fun insertAll() = runBlocking {
        insertRule1()
        insertRule2()
        insertRule3()
        insertRule4()
        insertRule5()
        insertRule6()
        insertRule7()
    }

    @Before
    fun createDatabase() {
        val context: Context = ApplicationProvider.getApplicationContext()

        ruleDatabase = Room.inMemoryDatabaseBuilder(context, RuleDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        ruleDatabase.apply {
            baseRuleDao = baseRuleDao()

            allUrlParamsRuleDao = allUrlParamsRuleDao()
            domainNameAndAllUrlParamsRuleDao = domainNameAndAllUrlParamsRuleDao()
            domainNameAndSpecificUrlParamsRuleDao = domainNameAndSpecificUrlParamsRuleDao()
            domainNameRuleDao = domainNameRuleDao()
            specificUrlParamsRuleDao = specificUrlParamsRuleDao()
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        ruleDatabase.close()
    }

    @Test
    @MediumTest
    @Throws(Exception::class)
    fun db_insertSingle_AllUrlParamsRule_successfullyInserts() = runBlocking {
        insertRule1()

        val allRules = allUrlParamsRuleDao.getAll().first() // collect the first emission
        val record = allRules.entries.first().toPair()
        assertEquals(rule1_allUrlParams, record)
    }

    @Test
    @MediumTest
    @Throws(Exception::class)
    fun db_insertAll_AllUrlParamsRule_returnsAllInsertedRules() = runBlocking {
        insertRule1()
        insertRule2()
        insertRule4()

        val allRules = allUrlParamsRuleDao.getAll().first()

        val record1 = allRules.entries.find { it.key.id == 1L }?.toPair()
        assertEquals(rule1_allUrlParams, record1)

        val record2 = allRules.entries.find { it.key.id == 2L }?.toPair()
        assertEquals(rule2_allUrlParams, record2)

        val record4 = allRules.entries.find { it.key.id == 4L }?.toPair()
        assertEquals(rule4_allUrlParams, record4)
    }

    @Test
    @MediumTest
    @Throws(Exception::class)
    fun db_insertSingle_DomainNameAndAllUrlParamsRule_successfullyInserts() = runBlocking {
        insertRule5()

        val allRules = domainNameAndAllUrlParamsRuleDao.getAll().first()
        val record = allRules.entries.first().toPair()
        assertEquals(rule5_domainNameAndAllUrlParams, record)
    }

    @Test
    @MediumTest
    @Throws(Exception::class)
    fun db_insertSingle_DomainNameAndSpecificUrlParamsRule_successfullyInserts() = runBlocking {
        insertRule6()

        val allRules = domainNameAndSpecificUrlParamsRuleDao.getAll().first()
        val record = allRules.entries.first().toPair()
        assertEquals(rule6_domainNameAndSpecificUrlParams, record)
    }

    @Test
    @MediumTest
    @Throws(Exception::class)
    fun db_insertSingle_SpecificUrlParamsRule_successfullyInserts() = runBlocking {
        insertRule7()

        val allRules = specificUrlParamsRuleDao.getAll().first()
        val record = allRules.entries.first().toPair()
        assertEquals(rule7_specificUrlParams, record)
    }

    @Test
    @MediumTest
    @Throws(Exception::class)
    fun db_insertAll_successfullyInserts_returnsWholeDataset() = runBlocking {
        insertAll()

        val allUrlParamsRules = allUrlParamsRuleDao.getAll().first()
        val domainNameAndAllUrlParamsRules = domainNameAndAllUrlParamsRuleDao.getAll().first()
        val domainNameAndSpecificUrlParamsRules =
            domainNameAndSpecificUrlParamsRuleDao.getAll().first()
        val domainNameRules = domainNameRuleDao.getAll().first()
        val specificUrlParamsRules = specificUrlParamsRuleDao.getAll().first()

        val record1 = allUrlParamsRules.entries.find {
            it.key.id == rule1_allUrlParams.first.id && it.value.id == rule1_allUrlParams.second.id
        }?.toPair()
        assertEquals(rule1_allUrlParams, record1)

        val record2 = allUrlParamsRules.entries.find {
            it.key.id == rule2_allUrlParams.first.id && it.value.id == rule2_allUrlParams.second.id
        }?.toPair()
        assertEquals(rule2_allUrlParams, record2)

        val record3 = domainNameRules.entries.find {
            it.key.id == rule3_domainName.first.id && it.value.id == rule3_domainName.second.id
        }?.toPair()
        assertEquals(rule3_domainName, record3)

        val record4 = allUrlParamsRules.entries.find {
            it.key.id == rule4_allUrlParams.first.id && it.value.id == rule4_allUrlParams.second.id
        }?.toPair()
        assertEquals(rule4_allUrlParams, record4)

        val record5 = domainNameAndAllUrlParamsRules.entries.find {
            it.key.id == rule5_domainNameAndAllUrlParams.first.id
                    && it.value.id == rule5_domainNameAndAllUrlParams.second.id
        }?.toPair()
        assertEquals(rule5_domainNameAndAllUrlParams, record5)

        val record6 = domainNameAndSpecificUrlParamsRules.entries.find {
            it.key.id == rule6_domainNameAndSpecificUrlParams.first.id
                    && it.value.id == rule6_domainNameAndSpecificUrlParams.second.id
        }?.toPair()
        assertEquals(rule6_domainNameAndSpecificUrlParams, record6)

        val record7 = specificUrlParamsRules.entries.find {
            it.key.id == rule7_specificUrlParams.first.id
                    && it.value.id == rule7_specificUrlParams.second.id
        }?.toPair()
        assertEquals(rule7_specificUrlParams, record7)
    }

    @Test
    @MediumTest
    @Throws(Exception::class)
    fun db_updateAllUrlParamsRule_updatesRecord() = runBlocking {
        insertRule1()

        val updatedBaseRule =
            rule1_allUrlParams.first.copy(title = "Test Rule", isLocalOnly = false)
        baseRuleDao.update(updatedBaseRule)

        val record = allUrlParamsRuleDao.getAll().first().entries.first().toPair()
        assertEquals(record, Pair(updatedBaseRule, record.second))
    }

    @Test
    @MediumTest
    @Throws(Exception::class)
    fun db_updateDomainNameAndAllUrlParamsRule_updatesRecord() = runBlocking {
        insertRule5()

        val updatedBaseRule = rule5_domainNameAndAllUrlParams.first.copy(
            title = "Android Test Rule",
            isLocalOnly = false
        )
        val updatedRule = rule5_domainNameAndAllUrlParams.second.copy(
            initialDomainName = "s.android.com",
            targetDomainName = "source.android.com"
        )
        baseRuleDao.update(updatedBaseRule)
        domainNameAndAllUrlParamsRuleDao.update(updatedRule)

        val record = domainNameAndAllUrlParamsRuleDao.getAll().first().entries.first().toPair()
        assertEquals(record, Pair(updatedBaseRule, updatedRule))
    }

    @Test
    @MediumTest
    @Throws(Exception::class)
    fun db_updateDomainNameAndSpecificUrlParamsRule_updatesRecord() = runBlocking {
        insertRule6()

        val updatedBaseRule = rule6_domainNameAndSpecificUrlParams.first.copy(
            title = "Domain Name And Specific URL Params Rule",
            dateModified = 0,
            isLocalOnly = false
        )
        val updatedRule = rule6_domainNameAndSpecificUrlParams.second.copy(
            targetDomainName = "blog.google",
            removableParams = listOf("a", "b", "34895848uhdfh83", "c", "99928373893x38s")
        )
        baseRuleDao.update(updatedBaseRule)
        domainNameAndSpecificUrlParamsRuleDao.update(updatedRule)

        val record = domainNameAndSpecificUrlParamsRuleDao.getAll().first().entries.first().toPair()
        assertEquals(record, Pair(updatedBaseRule, updatedRule))
    }

    @Test
    @MediumTest
    @Throws(Exception::class)
    fun db_updateDomainNameRule_updatesRecord() = runBlocking {
        insertRule3()

        val updatedBaseRule = rule3_domainName.first.copy(
            title = "Domain Name Rule",
            dateModified = 0,
            isLocalOnly = false
        )
        val updatedRule = rule3_domainName.second.copy(
            targetDomainName = "twitter.com"
        )
        baseRuleDao.update(updatedBaseRule)
        domainNameRuleDao.update(updatedRule)

        val record = domainNameRuleDao.getAll().first().entries.first().toPair()
        assertEquals(record, Pair(updatedBaseRule, updatedRule))
    }

    @Test
    @MediumTest
    @Throws(Exception::class)
    fun db_updateSpecificUrlParamsRule_updatesRecord() = runBlocking {
        insertRule7()

        val updatedBaseRule = rule7_specificUrlParams.first.copy(
            title = "Domain Name Rule",
            dateModified = 1230001239,
        )
        val updatedRule = rule7_specificUrlParams.second.copy(
            removableParams = listOf(
                "hello",
                "world",
                "!",
                "we",
                "are",
                "communicating",
                "through",
                "url",
                "params"
            )
        )
        baseRuleDao.update(updatedBaseRule)
        specificUrlParamsRuleDao.update(updatedRule)

        val record = specificUrlParamsRuleDao.getAll().first().entries.first().toPair()
        assertEquals(record, Pair(updatedBaseRule, updatedRule))
    }

    @Test
    @MediumTest
    @Throws(Exception::class)
    fun db_deleteAllRules_deletesEverythingInDatabase() = runBlocking {
        insertAll()
        baseRuleDao.deleteAll()

        val allUrlParamsRules = allUrlParamsRuleDao.getAll().first()
        assertTrue(allUrlParamsRules.isEmpty())

        val domainNameAndAllUrlParamsRules = domainNameAndAllUrlParamsRuleDao.getAll().first()
        assertTrue(domainNameAndAllUrlParamsRules.isEmpty())

        val domainNameAndSpecificUrlParamsRules =
            domainNameAndSpecificUrlParamsRuleDao.getAll().first()
        assertTrue(domainNameAndSpecificUrlParamsRules.isEmpty())

        val domainNameRules = domainNameRuleDao.getAll().first()
        assertTrue(domainNameRules.isEmpty())

        val specificUrlParamsRules = specificUrlParamsRuleDao.getAll().first()
        assertTrue(specificUrlParamsRules.isEmpty())
    }

    // TODO: test deletion of a specific rule - should delete the record in the specific rule table
    //  as well as the associated base rule.

    companion object Dataset {
        private const val DEFAULT_AUTHOR_ID = "local_user"

        private val rule1_allUrlParams = Pair(
            BaseRule(
                id = 1,
                title = "My rule",
                authorId = DEFAULT_AUTHOR_ID,
                mutationType = MutationType.URL_PARAMS_ALL,
                triggerDomain = "instagram.com",
                isLocalOnly = true,
                isEnabled = true,
            ),
            AllUrlParamsRule(id = 1, baseRuleId = 1)
        )

        private val rule2_allUrlParams = Pair(
            BaseRule(
                id = 2,
                title = "My second rule",
                authorId = DEFAULT_AUTHOR_ID,
                mutationType = MutationType.URL_PARAMS_ALL,
                triggerDomain = "spotify.com",
                isLocalOnly = true,
                isEnabled = true,
            ),
            AllUrlParamsRule(id = 2, baseRuleId = 2)
        )

        private val rule3_domainName = Pair(
            BaseRule(
                id = 3,
                title = "twitter.com to x.com",  // said no one ever (other than Husk)
                authorId = DEFAULT_AUTHOR_ID,
                mutationType = MutationType.DOMAIN_NAME,
                triggerDomain = "twitter.com",
                isEnabled = true,
                isLocalOnly = true
            ),
            DomainNameRule(
                id = 1,
                baseRuleId = 3,
                initialDomainName = "twitter.com",
                targetDomainName = "x.com"
            )
        )

        private val rule4_allUrlParams = Pair(
            BaseRule(
                id = 4,
                title = "Medium rule",
                authorId = DEFAULT_AUTHOR_ID,
                mutationType = MutationType.URL_PARAMS_ALL,
                triggerDomain = "medium.com",
                isEnabled = true,
                isLocalOnly = true
            ),
            AllUrlParamsRule(id = 3, baseRuleId = 4)
        )

        private val rule5_domainNameAndAllUrlParams = Pair(
            BaseRule(
                id = 5,
                title = "Android Developers",
                authorId = DEFAULT_AUTHOR_ID,
                mutationType = MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL,
                triggerDomain = "d.android.com",
                isEnabled = true,
                isLocalOnly = true
            ),
            DomainNameAndAllUrlParamsRule(
                id = 1,
                baseRuleId = 5,
                initialDomainName = "d.android.com",
                targetDomainName = "developer.android.com"
            )
        )

        private val rule6_domainNameAndSpecificUrlParams = Pair(
            BaseRule(
                id = 6,
                title = "Google Search",
                authorId = DEFAULT_AUTHOR_ID,
                mutationType = MutationType.DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC,
                triggerDomain = "google.com",
                isEnabled = true,
                isLocalOnly = true
            ),
            DomainNameAndSpecificUrlParamsRule(
                id = 1,
                baseRuleId = 6,
                initialDomainName = "google.com",
                targetDomainName = "google.co.uk",
                removableParams = listOf("gs_lcrp", "sourceid", "ie")
            )
        )

        private val rule7_specificUrlParams = Pair(
            BaseRule(
                id = 7,
                title = "YouTube",
                authorId = DEFAULT_AUTHOR_ID,
                mutationType = MutationType.URL_PARAMS_SPECIFIC,
                triggerDomain = "youtube.com",
                isEnabled = true,
                isLocalOnly = true
            ),
            SpecificUrlParamsRule(
                id = 1,
                baseRuleId = 7,
                removableParams = listOf("list")
            )
        )
    }
}