package com.suvanl.fixmylinks.domain.mutation

import com.suvanl.fixmylinks.domain.mutation.model.AllUrlParamsMutation
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutation
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndSpecificUrlParamsMutation
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutation
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutation
import com.suvanl.fixmylinks.domain.mutation.rule.BuiltInRules
import com.suvanl.fixmylinks.domain.util.UriUtils.removeSubdomain
import java.net.URI

class MutateUriUseCase {
    operator fun invoke(uri: URI): URI {
        val uriUsesWwwSubdomain = uri.host.startsWith("www.")
        val uriToMutate = if (uriUsesWwwSubdomain) {
            uri.removeSubdomain("www")
        } else uri

        val triggeredRule = findRule(uriToMutate) ?: return uriToMutate

        val removeAllUrlParamsUseCase = RemoveAllUrlParamsUseCase()
        val removeSpecificUrlParamsUseCase = RemoveSpecificUrlParamsUseCase()
        val replaceDomainNameUseCase = ReplaceDomainNameUseCase()

        when (triggeredRule) {
            is DomainNameMutation -> {
                return replaceDomainNameUseCase(
                    uri = uriToMutate,
                    mutationInfo = triggeredRule.mutationInfo,
                    useWwwSubdomain = uriUsesWwwSubdomain
                )
            }

            is AllUrlParamsMutation -> {
                return removeAllUrlParamsUseCase(uriToMutate)
            }

            is SpecificUrlParamsMutation -> {
                return removeSpecificUrlParamsUseCase(
                    uri = uriToMutate,
                    paramsToRemove = triggeredRule.mutationInfo.removableParams,
                    useWwwSubdomain = uriUsesWwwSubdomain
                )
            }

            is DomainNameAndAllUrlParamsMutation -> {
                val uriWithMutatedDomainName = replaceDomainNameUseCase(
                    uri = uriToMutate,
                    mutationInfo = triggeredRule.mutationInfo,
                    useWwwSubdomain = uriUsesWwwSubdomain
                )

                // first remove url params
                val parameterlessUri = removeAllUrlParamsUseCase(uriWithMutatedDomainName)

                // then replace the domain name as per the rule's mutationInfo
                return replaceDomainNameUseCase(
                    uri = parameterlessUri,
                    mutationInfo = triggeredRule.mutationInfo,
                    useWwwSubdomain = uriUsesWwwSubdomain
                )
            }

            is DomainNameAndSpecificUrlParamsMutation -> {
                throw NotImplementedError(
                    "DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC is not currently supported"
                )
            }

            else -> return uriToMutate
        }
    }

    /**
     * Finds a rule for the given initial [uri]
     */
    // TODO: take in a `customRules` parameter so that we can search within these too
    private fun findRule(uri: URI) = BuiltInRules.all.find { it.triggerDomain == uri.host }
}
