package com.suvanl.fixmylinks.ui.navigation.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseOutQuint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry
import com.suvanl.fixmylinks.ui.navigation.addNewRuleFlowScreens
import com.suvanl.fixmylinks.ui.navigation.getBaseRoute
import com.suvanl.fixmylinks.ui.navigation.ruleDetailsFlowScreens
import com.suvanl.fixmylinks.ui.util.topLevelScreensWithFab

enum class NavigationEnterTransitionMode { ENTER, POP_ENTER }
enum class NavigationExitTransitionMode { EXIT, POP_EXIT }

private val userFlowScreens = listOf(addNewRuleFlowScreens, ruleDetailsFlowScreens).flatten()

/**
 * Returns the appropriate navigation [ExitTransition] based on the given [transitionMode] and the
 * initial and target states (from the current [transitionScope]).
 */
fun exitNavigationTransition(
    transitionMode: NavigationExitTransitionMode,
    transitionScope: AnimatedContentTransitionScope<NavBackStackEntry>
): ExitTransition {
    val initialDestinationRoute = getBaseRoute(transitionScope.initialState.destination.route)
    val targetDestinationRoute = getBaseRoute(transitionScope.targetState.destination.route)

    // Whether we're navigating from a screen within a user flow to a top-level screen
    // that has a FAB
    val isNavigatingFromFlowToTopLevelDestination =
        userFlowScreens.any { it.route == initialDestinationRoute }
                && topLevelScreensWithFab.any { it.route == targetDestinationRoute }

    // If the target destination is part of a user flow, or if we're navigating back to
    // a screen that this flow could've been started from
    return if (
        userFlowScreens.any { it.route == targetDestinationRoute }
        || isNavigatingFromFlowToTopLevelDestination
    ) {
        // Perform slide out animation when navigating from an initial state of a screen that is
        // part of the "add new rule" flow, and the target destination is a screen that this flow
        // could be started from (i.e., a screen with a FAB, since clicking on the FAB is what
        // starts this flow), or if we're performing intra-flow navigation.
        transitionScope.slideOutOfContainer(
            towards = when (transitionMode) {
                NavigationExitTransitionMode.EXIT -> {
                    AnimatedContentTransitionScope.SlideDirection.Left
                }

                NavigationExitTransitionMode.POP_EXIT -> {
                    AnimatedContentTransitionScope.SlideDirection.Right
                }
            },
            animationSpec = spring(
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = 100,
                easing = EaseOutQuint
            )
        )
    } else {
        return when (transitionMode) {
            NavigationExitTransitionMode.EXIT -> {
                scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
            }

            NavigationExitTransitionMode.POP_EXIT -> {
                scaleOutOfContainer()
            }
        }
    }
}

/**
 * Returns the appropriate navigation [EnterTransition] based on the given [transitionMode] and the
 * initial and target states (from the current [transitionScope]).
 */
fun enterNavigationTransition(
    transitionMode: NavigationEnterTransitionMode,
    transitionScope: AnimatedContentTransitionScope<NavBackStackEntry>
): EnterTransition {
    val initialDestinationRoute = getBaseRoute(transitionScope.initialState.destination.route)
    val targetDestinationRoute = getBaseRoute(transitionScope.targetState.destination.route)

    // If we're navigating from a destination that's part of a user flow back to a top-level destination
    val isNavigatingFromFlowToTopLevelDestination =
        userFlowScreens.any { it.route == initialDestinationRoute }
                && topLevelScreensWithFab.any { it.route == targetDestinationRoute }

    // If the target destination is part of a user flow, or if we're navigating out of a flow
    // (back to a top-level destination)
    return if (
        userFlowScreens.any { it.route == targetDestinationRoute }
        || isNavigatingFromFlowToTopLevelDestination
    ) {
        transitionScope.slideIntoContainer(
            towards = when (transitionMode) {
                NavigationEnterTransitionMode.ENTER -> {
                    AnimatedContentTransitionScope.SlideDirection.Left
                }

                NavigationEnterTransitionMode.POP_ENTER -> {
                    AnimatedContentTransitionScope.SlideDirection.Right
                }
            },
            animationSpec = tween(
                durationMillis = 500,
                easing = EaseOutQuint
            )
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = 500,
                easing = EaseOutQuint
            )
        )
    } else {
        return when (transitionMode) {
            NavigationEnterTransitionMode.ENTER -> {
                scaleIntoContainer()
            }

            NavigationEnterTransitionMode.POP_ENTER -> {
                scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
            }
        }
    }
}