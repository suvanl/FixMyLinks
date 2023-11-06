package com.suvanl.fixmylinks.domain.mutation

import com.suvanl.fixmylinks.domain.mutation.model.DomainNameMutationInfo
import java.net.URI

class ReplaceDomainNameUseCase {
    operator fun invoke(
        uri: URI,
        mutationInfo: DomainNameMutationInfo,
        useWwwSubdomain: Boolean
    ): URI {
        uri.apply {
            return MutatedUri(
                scheme = scheme,
                host = if (useWwwSubdomain) "www.${mutationInfo.to}" else mutationInfo.to,
                path = path,
                rawQuery = rawQuery
            ).build()
        }
    }
}