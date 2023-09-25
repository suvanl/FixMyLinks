package com.suvanl.fixmylinks

import com.suvanl.fixmylinks.domain.mutation.MutateUriUseCase
import com.suvanl.fixmylinks.domain.mutation.MutationType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.URI

/**
 * Tests for link mutation business logic
 */
class LinkMutationTests {
    @Test
    fun `remove ONLY 'igshid' parameter from Instagram link with multiple parameters`() {
        val mutateUriUseCase = MutateUriUseCase()
        val actual = mutateUriUseCase(
            URI("https://www.instagram.com/reel/CxdeuhUrrE6/?igshid=MTc4MmM1YmI2Ng==&hl=en"),
            MutationType.URL_PARAMS_SPECIFIC
        )
        val expected = URI("https://instagram.com/reel/CxdeuhUrrE6/?hl=en")

        assertEquals(expected, actual)
    }

    @Test
    fun `remove ONLY 'igshid' parameter from Instagram link with multiple parameters (reversed param order)`() {
        val mutateUriUseCase = MutateUriUseCase()
        val actual = mutateUriUseCase(
            URI("https://instagram.com/reel/CxdeuhUrrE6/?hl=en&igshid=MTc4MmM1YmI2Ng=="),
            MutationType.URL_PARAMS_SPECIFIC
        )
        val expected = URI("https://instagram.com/reel/CxdeuhUrrE6/?hl=en")

        assertEquals(expected, actual)
    }

    @Test
    fun `remove lone 'igshid' parameter from Instagram link using URL_PARAMS_SPECIFIC MutationType`() {
        val mutateUriUseCase = MutateUriUseCase()
        val actual = mutateUriUseCase(
            URI("https://instagram.com/reel/CxdeuhUrrE6/?igshid=MTc4MmM1YmI2Ng=="),
            MutationType.URL_PARAMS_SPECIFIC
        )
        val expected = URI("https://instagram.com/reel/CxdeuhUrrE6/")

        assertEquals(expected, actual)
    }

    @Test
    fun `remove lone 'igshid' parameter from Instagram link with urlencoded chars using URL_PARAMS_SPECIFIC MutationType`() {
        val mutateUriUseCase = MutateUriUseCase()
        val actual = mutateUriUseCase(
            URI("https://instagram.com/reel/CxdeuhUrrE6/?igshid=MTc4MmM1YmI2Ng%3D%3D"),
            MutationType.URL_PARAMS_SPECIFIC
        )
        val expected = URI("https://instagram.com/reel/CxdeuhUrrE6/")

        assertEquals(expected, actual)
    }

    @Test
    fun `change x(dot)com domain name to twitter(dot)com and remove all URL parameters`() {
        val mutateUriUseCase = MutateUriUseCase()
        val actual = mutateUriUseCase(
            URI("https://x.com/Android/status/1704894351976137098?t=TZO0gtzWyOO95dFV8JzGXw&s=09"),
            MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL
        )
        val expected = URI("https://twitter.com/Android/status/1704894351976137098")

        assertEquals(expected, actual)
    }
}