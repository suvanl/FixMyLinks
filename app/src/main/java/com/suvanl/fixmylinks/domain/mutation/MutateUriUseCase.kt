package com.suvanl.fixmylinks.domain.mutation

import java.net.URI

class MutateUriUseCase {
    operator fun invoke(uri: URI, stripUrlParams: Boolean = true): URI {
        val mutation = SupportedMutation.values().find {
            uri.host == it.info.from
        } ?: return uri

        var mutatedUri = uri.toString()
            .replace(uri.host, mutation.info.to)

        if (stripUrlParams) mutatedUri = mutatedUri.split("?")[0]

        return URI(mutatedUri)
    }
}
