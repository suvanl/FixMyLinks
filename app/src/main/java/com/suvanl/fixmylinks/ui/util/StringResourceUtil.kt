package com.suvanl.fixmylinks.ui.util

import androidx.annotation.StringRes
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.MutationType


object StringResourceUtil {
    // Required to guarantee type safety in maps (such as humanizedMutationTypeName) because
    // otherwise the type of the value will simply be inferred as a standard `Int`, rather than a
    // string resource ID.
    data class StringResource(@StringRes val id: Int)

    val humanizedMutationTypeNames = mapOf(
        MutationType.DOMAIN_NAME to StringResource(R.string.mt_domain_name),
        MutationType.URL_PARAMS_ALL to StringResource(R.string.mt_url_params_all),
        MutationType.URL_PARAMS_SPECIFIC to StringResource(R.string.mt_url_params_specific),
        MutationType.DOMAIN_NAME_AND_URL_PARAMS_ALL to StringResource(
            R.string.mt_domain_name_and_url_params_all
        )
    )
}