package com.suvanl.fixmylinks.ui.util

import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationInfo
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationModel

/**
 * Common dummy data to use for composable design previews
 */
object PreviewData {
    val previewRules = listOf(
        DomainNameAndAllUrlParamsMutationModel(
            name = "Google rule",
            triggerDomain = "google.com",
            isLocalOnly = true,
            isEnabled = true,
            dateModifiedTimestamp = 1700174822,
            mutationInfo = DomainNameMutationInfo(
                initialDomain = "google.com",
                targetDomain = "google.co.uk"
            )
        ),
        SpecificUrlParamsMutationModel(
            name = "YouTube - remove playlist association and timestamp, but nothing else",
            triggerDomain = "youtube.com",
            isLocalOnly = true,
            isEnabled = true,
            dateModifiedTimestamp = 1701970463,
            mutationInfo = SpecificUrlParamsMutationInfo(
                removableParams = listOf("list", "t")
            )
        ),
        SpecificUrlParamsMutationModel(
            name = "YouTube - remove timestamp",
            triggerDomain = "youtube.com",
            isLocalOnly = true,
            isEnabled = false,
            dateModifiedTimestamp = 1701970464,
            mutationInfo = SpecificUrlParamsMutationInfo(
                removableParams = listOf("t")
            )
        ),
    )
}