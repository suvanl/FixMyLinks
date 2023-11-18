package com.suvanl.fixmylinks

import com.suvanl.fixmylinks.domain.mutation.MutateUriUseCase
import com.suvanl.fixmylinks.domain.mutation.model.AllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.URI

/**
 * Test suite for link mutation business logic
 */
class LinkMutationTest {

    private lateinit var mutateUriUseCase: MutateUriUseCase

    private val mockCustomRules = listOf(
        AllUrlParamsMutationModel(
            name = "Medium rule",
            triggerDomain = "medium.com",
            isLocalOnly = true
        ),
        AllUrlParamsMutationModel(
            name = "Google Blog",
            triggerDomain = "blog.google",
            isLocalOnly = true
        ),
        DomainNameAndAllUrlParamsMutationModel(
            name = "Android Developers URL shortener and parameter remover",
            triggerDomain = "developer.android.com",
            isLocalOnly = true,
            mutationInfo = DomainNameMutationInfo(
                initialDomain = "developer.android.com",
                targetDomain = "d.android.com"
            )
        ),
    )

    @Before
    fun setup() {
        mutateUriUseCase = MutateUriUseCase()
    }

    @Test
    fun `remove ONLY 'igshid' parameter from Instagram link with multiple parameters`() {
        val actual = mutateUriUseCase(
            URI("https://www.instagram.com/reel/CxdeuhUrrE6/?igshid=MTc4MmM1YmI2Ng==&hl=en"),
            mockCustomRules
        )
        val expected = URI("https://www.instagram.com/reel/CxdeuhUrrE6/?hl=en")

        assertEquals(expected, actual)
    }

    @Test
    fun `remove ONLY 'igshid' parameter from Instagram link with multiple parameters (reversed param order)`() {
        val actual = mutateUriUseCase(
            URI("https://www.instagram.com/reel/CxdeuhUrrE6/?hl=en&igshid=MTc4MmM1YmI2Ng=="),
            mockCustomRules
        )
        val expected = URI("https://www.instagram.com/reel/CxdeuhUrrE6/?hl=en")

        assertEquals(expected, actual)
    }

    @Test
    fun `remove lone 'igshid' parameter from Instagram link using URL_PARAMS_SPECIFIC MutationType`() {
        val actual = mutateUriUseCase(
            URI("https://www.instagram.com/reel/CxdeuhUrrE6/?igshid=MTc4MmM1YmI2Ng=="),
            mockCustomRules
        )
        val expected = URI("https://www.instagram.com/reel/CxdeuhUrrE6/")

        assertEquals(expected, actual)
    }

    @Test
    fun `remove lone 'igshid' parameter from Instagram link with urlencoded chars using URL_PARAMS_SPECIFIC MutationType`() {
        val actual = mutateUriUseCase(
            URI("https://www.instagram.com/reel/CxdeuhUrrE6/?igshid=MTc4MmM1YmI2Ng%3D%3D"),
            mockCustomRules
        )
        val expected = URI("https://www.instagram.com/reel/CxdeuhUrrE6/")

        assertEquals(expected, actual)
    }

    @Test
    fun `change x(dot)com domain name to twitter(dot)com and remove all URL parameters`() {
        val actual = mutateUriUseCase(
            URI("https://x.com/Android/status/1704894351976137098?t=TZO0gtzWyOO95dFV8JzGXw&s=09"),
            mockCustomRules
        )
        val expected = URI("https://twitter.com/Android/status/1704894351976137098")

        assertEquals(expected, actual)
    }

    @Test
    fun `change www(dot)x(dot)com domain name to www(dot)twitter(dot)com and remove all URL parameters`() {
        val actual = mutateUriUseCase(
            URI("https://www.x.com/Android/status/1704894351976137098?t=TZO0gtzWyOO95dFV8JzGXw&s=09"),
            mockCustomRules
        )
        val expected = URI("https://www.twitter.com/Android/status/1704894351976137098")

        assertEquals(expected, actual)
    }

    @Test
    fun `attempt to remove URL parameters of a parameterless Spotify link`() {
        val actual = mutateUriUseCase(
            URI("https://open.spotify.com/track/3PRljkcobGtC6Kc3ILLSmq"),
            mockCustomRules
        )
        // URI should not change
        val expected = URI("https://open.spotify.com/track/3PRljkcobGtC6Kc3ILLSmq")

        assertEquals(expected, actual)
    }
}