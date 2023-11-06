package com.suvanl.fixmylinks.domain.mutation

import java.net.URI

class RemoveAllUrlParamsUseCase {
    operator fun invoke(uri: URI) = URI(uri.toString().split("?")[0])
}