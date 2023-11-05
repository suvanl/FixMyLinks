package com.suvanl.fixmylinks.ui.navigation

sealed class NestedNavGraphParent(val route: String) {
    data object NewRuleFlow : NestedNavGraphParent("new_rule_flow")
}