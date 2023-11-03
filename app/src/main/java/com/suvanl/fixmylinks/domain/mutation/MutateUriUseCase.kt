package com.suvanl.fixmylinks.domain.mutation

import com.suvanl.fixmylinks.domain.mutation.MutationType.*
import com.suvanl.fixmylinks.domain.util.UriUtils.removeSubdomain
import java.net.URI

class MutateUriUseCase {
    operator fun invoke(uri: URI, mutationType: MutationType): URI {
        val uriUsesWwwSubdomain = uri.host.startsWith("www.")
        val uriToMutate = if (uriUsesWwwSubdomain) {
            uri.removeSubdomain("www")
        } else uri

        when (mutationType) {
            DOMAIN_NAME -> {
                return mutateDomainName(
                    uri = uriToMutate,
                    useWwwSubdomain = uriUsesWwwSubdomain
                )
            }

            URL_PARAMS_ALL -> {
                return URI(removeAllUrlParams(uriToMutate.toString()))
            }

            URL_PARAMS_SPECIFIC -> {
                val paramsToRemove =
                    MutationMap.UriToRemovableParametersList.getOrDefault(
                        uriToMutate.host,
                        listOf()
                    )

                return removeSpecificUrlParams(
                    uri = uriToMutate,
                    paramsToRemove = paramsToRemove,
                    useWwwSubdomain = uriUsesWwwSubdomain
                )
            }

            DOMAIN_NAME_AND_URL_PARAMS_ALL -> {
                val uriWithMutatedDomainName = mutateDomainName(
                    uri = uriToMutate,
                    useWwwSubdomain = uriUsesWwwSubdomain
                )
                return URI(removeAllUrlParams(uriWithMutatedDomainName.toString()))
            }

            DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC -> {
                throw NotImplementedError(
                    "DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC is not currently supported"
                )
            }

            FALLBACK -> return uriToMutate
        }
    }

    private fun removeAllUrlParams(link: String) = link.split("?")[0]

    private fun removeSpecificUrlParams(
        uri: URI,
        paramsToRemove: List<String>,
        useWwwSubdomain: Boolean
    ): URI {
        if (uri.rawQuery == null || paramsToRemove.isEmpty()) return uri

        val queryString = uri.rawQuery

        // If uri only contains one parameter
        // TODO: increase the stringency of this check?
        if (!queryString.contains("&")) {
            // set `limit` to 2 to ensure the resultant List contains 2 elements at most
            val paramName = queryString.split("=", limit = 2)[0]

            if (!paramsToRemove.contains(paramName)) {
                return MutatedUri(
                    scheme = uri.scheme,
                    host = if (useWwwSubdomain) "www.${uri.host}" else uri.host,
                    path = uri.path,
                    rawQuery = uri.rawQuery
                ).build()
            }

            // if `paramsToRemove` does contain the name of this param, we'll remove the entire
            // query string (since it only had one parameter, and a parameter with this name exists
            // within the `paramsToRemove` List, meaning it should be removed).
            return URI(
                removeAllUrlParams(
                    link = MutatedUri(
                        scheme = uri.scheme,
                        host = if (useWwwSubdomain) "www.${uri.host}" else uri.host,
                        path = uri.path,
                        rawQuery = uri.rawQuery
                    ).build().toString()
                )
            )
        }

        // Create a List of `Pair`s of URL params
        val paramPairs = mutableListOf<Pair<String, String>>()

        // Populate the List of Pairs
        queryString.split("&").map {
            // [0] = key; [1] = value
            val keyValueList = it.split("=", limit = 2)

            // Use firstOrNull in case queryString is malformed
            val key = keyValueList.firstOrNull() ?: ""
            val value = keyValueList.drop(1).firstOrNull() ?: ""

            paramPairs.add(Pair(key, value))
        }

        // Create a map of URL param key/value pairs that should be present in the mutated URL
        val requiredUrlParams = paramPairs.toMap().filter { (key, _) ->
            // filter out keys that should be removed
            !paramsToRemove.contains(key)
        }

        // If no params need to be present in the mutated URL (since all params in the URL exist in
        // `paramsToRemove`), return the URL without any params
        if (requiredUrlParams.isEmpty()) return URI(removeAllUrlParams(uri.toString()))

        // Format the map of new params in the correct format required for URLs
        val mutatedQueryString = requiredUrlParams.entries.joinToString("&") { (key, value) ->
            "$key=$value"
        }

        uri.apply {
            // Rebuild the URI with the newly mutated query string
            return MutatedUri(
                scheme = scheme,
                host = if (useWwwSubdomain) "www.$host" else host,
                path = path,
                rawQuery = mutatedQueryString
            ).build()
        }
    }

    private fun mutateDomainName(uri: URI, useWwwSubdomain: Boolean): URI {
        val originalUri = MutatedUri(
            scheme = uri.scheme,
            host = if (useWwwSubdomain) "www.${uri.host}" else uri.host,
            path = uri.path,
            rawQuery = uri.rawQuery
        ).build()

        // Find the domain name mutation
        val mutation = DomainNameMutation.entries.find {
            uri.host == it.info.from
        } ?: return originalUri

        uri.apply {
            return MutatedUri(
                scheme = scheme,
                host = if (useWwwSubdomain) "www.${mutation.info.to}" else mutation.info.to,
                path = path,
                rawQuery = rawQuery
            ).build()
        }
    }
}
