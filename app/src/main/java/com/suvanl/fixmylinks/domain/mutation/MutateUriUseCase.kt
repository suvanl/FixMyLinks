package com.suvanl.fixmylinks.domain.mutation

import com.suvanl.fixmylinks.domain.mutation.MutationType.*
import java.net.URI

class MutateUriUseCase {
    operator fun invoke(uri: URI, mutationType: MutationType): URI {
        when (mutationType) {
            DOMAIN_NAME -> {
                return mutateDomainName(uri)
            }
            URL_PARAMS_ALL -> {
                return URI(removeAllUrlParams(uri.toString()))
            }
            URL_PARAMS_SPECIFIC -> {
                throw NotImplementedError("URL_PARAMS_SPECIFIC is not currently supported")
            }
            DOMAIN_NAME_AND_URL_PARAMS_ALL -> {
                val uriWithMutatedDomainName = mutateDomainName(uri)
                return URI(removeAllUrlParams(uriWithMutatedDomainName.toString()))
            }
            DOMAIN_NAME_AND_URL_PARAMS_SPECIFIC -> {
                throw NotImplementedError("URL_PARAMS_SPECIFIC is not currently supported")
            }
            FALLBACK -> return uri
        }
    }

    private fun removeAllUrlParams(link: String) = link.split("?")[0]

    private fun mutateDomainName(uri: URI): URI {
        // Find the domain name mutation
        val mutation = DomainNameMutation.values().find {
            uri.host == it.info.from
        } ?: return uri

        // Use `String.replace` to mutate the link
        val mutatedLink = uri.toString().replace(uri.host, mutation.info.to)

        // Convert the mutated link back to a URI and return it
        return URI(mutatedLink)
    }
}
