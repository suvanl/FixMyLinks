package com.suvanl.fixmylinks.ui.navigation.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.navigation.NavBackStackEntry
import com.suvanl.fixmylinks.ui.navigation.FmlScreen
import com.suvanl.fixmylinks.ui.navigation.addNewRuleFlowScreens

enum class NavigationEnterTransitionMode { ENTER, POP_ENTER }
enum class NavigationExitTransitionMode { EXIT, POP_EXIT }

private val screensWithFab = listOf(FmlScreen.Home, FmlScreen.Rules)

/**
 * Returns the appropriate navigation [ExitTransition] based on the given [transitionMode] and the
 * initial and target states (from the current [transitionScope]).
 */
fun exitNavigationTransition(
    transitionMode: NavigationExitTransitionMode,
    transitionScope: AnimatedContentTransitionScope<NavBackStackEntry>
): ExitTransition {
    // Whether we're navigating from a screen within the "add new rule" flow to a top-level screen
    // that has a FAB
    val isNavigatingFromFlowToTopLevelScreen =
        addNewRuleFlowScreens.any { it.route == transitionScope.initialState.destination.route }
                && screensWithFab.any { it.route == transitionScope.targetState.destination.route }

    // If the target destination is part of the "add new rule" flow, or if we're navigating back to
    // a screen that this flow could've been started from
    return if (
        addNewRuleFlowScreens.any { it.route == transitionScope.targetState.destination.route }
        || isNavigatingFromFlowToTopLevelScreen
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
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMediumLow
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
    // If we're navigating from a destination with the FAB to a destination in the "add new rule"
    // flow, or if we're performing intra-flow navigation, use the slide animation
    val isNavigatingFromTopLevelDestinationToFlow =
        screensWithFab.any { it.route == transitionScope.initialState.destination.route }
                && addNewRuleFlowScreens.any { it.route == transitionScope.targetState.destination.route }

    // Inverse of isNavigatingFromTopLevelDestinationToFlow
    val isNavigatingFromFlowToTopLevelDestination =
        addNewRuleFlowScreens.any { it.route == transitionScope.initialState.destination.route }
                && screensWithFab.any { it.route == transitionScope.targetState.destination.route }

    return if (
        isNavigatingFromTopLevelDestinationToFlow || isNavigatingFromFlowToTopLevelDestination
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
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMediumLow
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