package com.suvanl.fixmylinks.domain.mutation

import com.suvanl.fixmylinks.domain.mutation.model.AllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.BaseMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndAllUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameAndSpecificUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationModel
import com.suvanl.fixmylinks.domain.mutation.model.SpecificUrlParamsMutationModel
import com.suvanl.fixmylinks.domain.mutation.rule.BuiltInRules
import com.suvanl.fixmylinks.domain.util.UriUtils.removeSubdomain
import java.net.URI
import javax.inject.Inject

class MutateUriUseCase @Inject constructor() {
    operator fun invoke(uri: URI, customRules: List<BaseMutationModel>): URI {
        val uriUsesWwwSubdomain = uri.host.startsWith("www.")
        val uriToMutate = if (uriUsesWwwSubdomain) {
            uri.removeSubdomain("www")
        } else uri

        val triggeredRule = findRule(uriToMutate, customRules) ?: return uri

        val removeAllUrlParamsUseCase = RemoveAllUrlParamsUseCase()
        val removeSpecificUrlParamsUseCase = RemoveSpecificUrlParamsUseCase()
        val replaceDomainNameUseCase = ReplaceDomainNameUseCase()

        when (triggeredRule) {
            is DomainNameMutationModel -> {
                return replaceDomainNameUseCase(
                    uri = uriToMutate,
                    mutationInfo = triggeredRule.mutationInfo,
                    useWwwSubdomain = uriUsesWwwSubdomain
                )
            }

            is AllUrlParamsMutationModel -> {
                // using `uri` to ensure we keep the "www" subdomain if it exists in it
                return removeAllUrlParamsUseCase(uri)
            }

            is SpecificUrlParamsMutationModel -> {
                return removeSpecificUrlParamsUseCase(
                    uri = uriToMutate,
                    paramsToRemove = triggeredRule.mutationInfo.removableParams,
                    useWwwSubdomain = uriUsesWwwSubdomain
                )
            }

            is DomainNameAndAllUrlParamsMutationModel -> {
                // first remove url params
                val parameterlessUri = removeAllUrlParamsUseCase(uriToMutate)

                // then replace the domain name as per the rule's mutationInfo
                return replaceDomainNameUseCase(
                    uri = parameterlessUri,
                    mutationInfo = triggeredRule.mutationInfo,
                    useWwwSubdomain = uriUsesWwwSubdomain
                )
            }

            is DomainNameAndSpecificUrlParamsMutationModel -> {
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
    private fun findRule(uri: URI, customRules: List<BaseMutationModel>): BaseMutationModel? {
        val allRules = BuiltInRules.all + customRules
        return allRules.find { rule ->
            if (rule.triggerDomain.startsWith("*.")) {
                // Wildcard operator functionality: compare triggerDomain and URI.host without
                // subdomain on both sides of the equality check.
                // Note that URI.removeSubdomain("*") removes any subdomain that the URI.host may contain.
                rule.isEnabled && rule.triggerDomain.removePrefix("*.") == uri.removeSubdomain("*").host
            } else {
                rule.isEnabled && rule.triggerDomain == uri.host
            }
        }
    }
}
