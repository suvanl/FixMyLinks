package com.suvanl.fixmylinks.domain.mutation

import com.suvanl.fixmylinks.domain.mutation.model.AllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndSpecificUrlParamsMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndSpecificUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.URI

/**
 * Test suite for link mutation business logic
 */
class MutateUriUseCaseTest {

    private lateinit var mutateUriUseCase: MutateUriUseCase

    private val mockCustomRules = listOf(
        AllUrlParamsMutationModel(
            name = "Reddit remove all params",
            triggerDomain = "reddit.com",
            isLocalOnly = true,
            isEnabled = true,
        ),
        AllUrlParamsMutationModel(
            name = "Google Blog",
            triggerDomain = "blog.google",
            isLocalOnly = true,
            isEnabled = true,
        ),
        DomainNameAndAllUrlParamsMutationModel(
            name = "Android Developers URL shortener and parameter remover",
            triggerDomain = "developer.android.com",
            isLocalOnly = true,
            isEnabled = true,
            mutationInfo = DomainNameMutationInfo(
                initialDomain = "developer.android.com",
                targetDomain = "d.android.com"
            )
        ),
        SpecificUrlParamsMutationModel(
            name = "YouTube remove 'list' param",
            triggerDomain = "*.youtube.com",
            isLocalOnly = true,
            isEnabled = true,
            mutationInfo = SpecificUrlParamsMutationInfo(
                removableParams = listOf("list")
            )
        ),
        DomainNameMutationModel(
            name = "Google.com to Google.co.uk",
            triggerDomain = "google.com",
            isLocalOnly = true,
            isEnabled = true,
            mutationInfo = DomainNameMutationInfo(
                initialDomain = "google.com",
                targetDomain = "google.co.uk"
            )
        ),
        DomainNameAndSpecificUrlParamsMutationModel(
            name = "Wikipedia convert mobile link to desktop and remove 's' parameter (attached " +
                    "by Twitter to outbound links)",
            triggerDomain = "en.m.wikipedia.org",
            isLocalOnly = true,
            isEnabled = true,
            mutationInfo = DomainNameAndSpecificUrlParamsMutationInfo(
                initialDomainName = "en.m.wikipedia.org",
                targetDomainName = "en.wikipedia.org",
                removableParams = listOf("s")
            )
        )
    )

    @Before
    fun setup() {
        mutateUriUseCase = MutateUriUseCase()
    }

    @Test
    fun `apply URL_PARAMS_ALL custom rule`() {
        val actual = mutateUriUseCase(
            URI("https://www.reddit.com/r/androiddev/comments/158s8lf/i_made_this_circular_scribble_pager_indicator_in/?utm_source=share&utm_medium=web2x&context=3"),
            mockCustomRules
        )
        val expected = URI("https://www.reddit.com/r/androiddev/comments/158s8lf/i_made_this_circular_scribble_pager_indicator_in/")
        assertEquals(expected, actual)
    }

    @Test
    fun `URL_PARAMS_ALL rule restores 'www' subdomain if original extracted URL contained it`() {
        val actual = mutateUriUseCase(
            URI("https://www.blog.google/test-item?s=09"),
            mockCustomRules
        )

        val expected = URI("https://www.blog.google/test-item")
        assertEquals(expected, actual)
    }

    @Test
    fun `apply DOMAIN_NAME_AND_URL_PARAMS_ALL custom rule`() {
        val actual = mutateUriUseCase(
            URI("https://developer.android.com/training/dependency-injection/hilt-testing?s=09"),
            mockCustomRules
        )
        val expected = URI("https://d.android.com/training/dependency-injection/hilt-testing")
        assertEquals(expected, actual)
    }

    @Test
    fun `apply URL_PARAMS_SPECIFIC custom rule`() {
        val actual = mutateUriUseCase(
            URI("https://www.youtube.com/watch?v=B91ztNPq_cs&list=PLWz5rJ2EKKc8L8WlmqPD6zPEyVSKrL5PJ"),
            mockCustomRules
        )
        val expected = URI("https://www.youtube.com/watch?v=B91ztNPq_cs")
        assertEquals(expected, actual)
    }

    @Test
    fun `apply URL_PARAMS_SPECIFIC custom rule, test wildcard functionality`() {
        val actual1 = mutateUriUseCase(
            URI("https://m.youtube.com/watch?v=B91ztNPq_cs&list=PLWz5rJ2EKKc8L8WlmqPD6zPEyVSKrL5PJ"),
            mockCustomRules
        )
        val expected1 = URI("https://m.youtube.com/watch?v=B91ztNPq_cs")
        assertEquals(expected1, actual1)

        val actual2 = mutateUriUseCase(
            URI("https://not-a-real-subdomain.youtube.com/watch?v=B91ztNPq_cs&list=PLWz5rJ2EKKc8L8WlmqPD6zPEyVSKrL5PJ"),
            mockCustomRules
        )
        val expected2 = URI("https://not-a-real-subdomain.youtube.com/watch?v=B91ztNPq_cs")
        assertEquals(expected2, actual2)
    }

    @Test
    fun `apply DOMAIN_NAME custom rule`() {
        val actual = mutateUriUseCase(
            URI("https://www.google.com/search?q=hello+world"),
            mockCustomRules
        )
        val expected = URI("https://www.google.co.uk/search?q=hello+world")
        assertEquals(expected, actual)
    }

    @Test(expected = NotImplementedError::class)
    fun `apply DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC custom rule`() {
        val actual = mutateUriUseCase(
            URI("https://en.m.wikipedia.org/wiki/Android_(operating_system)?s=09"),
            mockCustomRules
        )
        val expected = URI("https://en.wikipedia.org/wiki/Android_(operating_system)")
        assertEquals(expected, actual)
    }

    @Test
    fun `built-in rule - remove ONLY 'igshid' parameter from Instagram link with multiple parameters`() {
        val actual = mutateUriUseCase(
            URI("https://www.instagram.com/reel/CxdeuhUrrE6/?igshid=MTc4MmM1YmI2Ng==&hl=en"),
            mockCustomRules
        )
        val expected = URI("https://www.instagram.com/reel/CxdeuhUrrE6/?hl=en")

        assertEquals(expected, actual)
    }

    @Test
    fun `built-in rule - remove ONLY 'igshid' parameter from Instagram link with multiple parameters (reversed param order)`() {
        val actual = mutateUriUseCase(
            URI("https://www.instagram.com/reel/CxdeuhUrrE6/?hl=en&igshid=MTc4MmM1YmI2Ng=="),
            mockCustomRules
        )
        val expected = URI("https://www.instagram.com/reel/CxdeuhUrrE6/?hl=en")

        assertEquals(expected, actual)
    }

    @Test
    fun `built-in rule - remove lone 'igshid' parameter from Instagram link using URL_PARAMS_SPECIFIC MutationType`() {
        val actual = mutateUriUseCase(
            URI("https://www.instagram.com/reel/CxdeuhUrrE6/?igshid=MTc4MmM1YmI2Ng=="),
            mockCustomRules
        )
        val expected = URI("https://www.instagram.com/reel/CxdeuhUrrE6/")

        assertEquals(expected, actual)
    }

    @Test
    fun `built-in rule - remove lone 'igshid' parameter from Instagram link with urlencoded chars using URL_PARAMS_SPECIFIC MutationType`() {
        val actual = mutateUriUseCase(
            URI("https://www.instagram.com/reel/CxdeuhUrrE6/?igshid=MTc4MmM1YmI2Ng%3D%3D"),
            mockCustomRules
        )
        val expected = URI("https://www.instagram.com/reel/CxdeuhUrrE6/")

        assertEquals(expected, actual)
    }

    @Test
    fun `built-in rule - change x(dot)com domain name to twitter(dot)com and remove all URL parameters`() {
        val actual = mutateUriUseCase(
            URI("https://x.com/Android/status/1704894351976137098?t=TZO0gtzWyOO95dFV8JzGXw&s=09"),
            mockCustomRules
        )
        val expected = URI("https://twitter.com/Android/status/1704894351976137098")

        assertEquals(expected, actual)
    }

    @Test
    fun `built-in rule - change www(dot)x(dot)com domain name to www(dot)twitter(dot)com and remove all URL parameters`() {
        val actual = mutateUriUseCase(
            URI("https://www.x.com/Android/status/1704894351976137098?t=TZO0gtzWyOO95dFV8JzGXw&s=09"),
            mockCustomRules
        )
        val expected = URI("https://www.twitter.com/Android/status/1704894351976137098")

        assertEquals(expected, actual)
    }

    @Test
    fun `built-in rule - attempt to remove URL parameters of a parameterless Spotify link`() {
        val actual = mutateUriUseCase(
            URI("https://open.spotify.com/track/3PRljkcobGtC6Kc3ILLSmq"),
            mockCustomRules
        )
        // URI should not change
        val expected = URI("https://open.spotify.com/track/3PRljkcobGtC6Kc3ILLSmq")

        assertEquals(expected, actual)
    }

    @Test
    fun `no rule - link containing 'www' subdomain and no URL params doesn't modify original link`() {
        val actual = mutateUriUseCase(
            URI("https://www.framer.com/motion/guide-reduce-bundle-size/"),
            mockCustomRules
        )
        // URI should not change
        val expected = URI("https://www.framer.com/motion/guide-reduce-bundle-size/")

        assertEquals(expected, actual)
    }

    @Test
    fun `built-in rule - link containing 'www' subdomain and no URL params doesn't modify original link`() {
        val actual = mutateUriUseCase(
            URI("https://www.open.spotify.com/track/3PRljkcobGtC6Kc3ILLSmq"),
            mockCustomRules
        )
        // URI should not change
        val expected = URI("https://www.open.spotify.com/track/3PRljkcobGtC6Kc3ILLSmq")

        assertEquals(expected, actual)
    }
}