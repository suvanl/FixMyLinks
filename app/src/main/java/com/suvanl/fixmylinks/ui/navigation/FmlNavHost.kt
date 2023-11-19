package com.suvanl.fixmylinks.ui.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.domain.mutation.MutationType
import com.suvanl.fixmylinks.ui.components.appbar.ProvideAppBarActions
import com.suvanl.fixmylinks.ui.components.appbar.menu.HintsDropdownItem
import com.suvanl.fixmylinks.ui.components.appbar.menu.OverflowMenu
import com.suvanl.fixmylinks.ui.navigation.transition.NavigationEnterTransitionMode
import com.suvanl.fixmylinks.ui.navigation.transition.NavigationExitTransitionMode
import com.suvanl.fixmylinks.ui.navigation.transition.enterNavigationTransition
import com.suvanl.fixmylinks.ui.navigation.transition.exitNavigationTransition
import com.suvanl.fixmylinks.ui.screens.HomeScreen
import com.suvanl.fixmylinks.ui.screens.RulesScreen
import com.suvanl.fixmylinks.ui.screens.SavedScreen
import com.suvanl.fixmylinks.ui.screens.newruleflow.AddRuleScreen
import com.suvanl.fixmylinks.ui.screens.newruleflow.AddRuleScreenUiState
import com.suvanl.fixmylinks.ui.screens.newruleflow.SelectRuleTypeScreen
import com.suvanl.fixmylinks.ui.util.getNewRuleFlowViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.SelectRuleTypeViewModel
import kotlinx.coroutines.launch

@Composable
fun FmlNavHost(
    navController: NavHostController,
    windowWidthSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = FmlScreen.Home.route,
        enterTransition = {
            enterNavigationTransition(NavigationEnterTransitionMode.ENTER, this)
        },
        exitTransition = {
            exitNavigationTransition(NavigationExitTransitionMode.EXIT, this)
        },
        popEnterTransition = {
            enterNavigationTransition(NavigationEnterTransitionMode.POP_ENTER, this)
        },
        popExitTransition = {
            exitNavigationTransition(NavigationExitTransitionMode.POP_EXIT, this)
        },
        modifier = modifier
    ) {
        composable(route = FmlScreen.Home.route) {
            HomeScreen()
        }

        composable(route = FmlScreen.Rules.route) {
            RulesScreen()
        }

        composable(route = FmlScreen.Saved.route) {
            SavedScreen()
        }

        navigation(
            startDestination = FmlScreen.SelectRuleType.route,
            route = NestedNavGraphParent.NewRuleFlow.route
        ) {
            composable(route = FmlScreen.SelectRuleType.route) {
                val viewModel = viewModel<SelectRuleTypeViewModel>()
                val mutationType by viewModel.mutationType.collectAsStateWithLifecycle()

                val isCompactLayout = windowWidthSize == WindowWidthSizeClass.Compact

                // Show "Next" button as top app bar action on Medium and Expanded layouts
                ProvideAppBarActions(
                    shouldShowActions = !isCompactLayout
                ) {
                    Button(
                        onClick = {
                            navController.navigateSingleTop(
                                route = FmlScreen.AddRule.route,
                                popUpToStartDestination = false
                            )
                        }
                    ) {
                        Text(text = stringResource(id = R.string.next))
                    }

                    Spacer(modifier = Modifier.width(16.dp))
                }

                SelectRuleTypeScreen(
                    showNextButton = isCompactLayout,
                    onSelectedMutationTypeChanged = { selectedOption ->
                        viewModel.updateSelectedMutationType(
                            selection = MutationType.valueOf(selectedOption.id)
                        )
                    },
                    onNextButtonClick = {
                        navController.navigateSingleTop(
                            route = "${FmlScreen.AddRule.route}/${mutationType.name}",
                            popUpToStartDestination = false
                        )
                    }
                )
            }

            composable(
                route = FmlScreen.AddRule.routeWithArgs,
                arguments = FmlScreen.AddRule.args
            ) { navBackStackEntry ->
                val coroutineScope = rememberCoroutineScope()

                val mutationTypeArg =
                    navBackStackEntry.arguments?.getString(FmlScreen.AddRule.mutationTypeArg)

                val mutationType = MutationType.entries.find { it.name == mutationTypeArg }
                    ?: MutationType.FALLBACK

                val viewModel: AddRuleViewModel = getNewRuleFlowViewModel(mutationType)

                val isCompactLayout = windowWidthSize == WindowWidthSizeClass.Compact
                var hintsOptionCheckedState by remember { mutableStateOf(true) }

                fun handleSaveRuleClick() {
                    coroutineScope.launch {
                        viewModel.saveRule()
                        navController.popBackStack(
                            route = NestedNavGraphParent.NewRuleFlow.route,
                            inclusive = true
                        )
                    }
                }

                ProvideAppBarActions {
                    if (!isCompactLayout) {
                        Button(
                            onClick = { handleSaveRuleClick() }
                        ) {
                            Text(text = stringResource(id = R.string.save))
                        }
                    }

                    OverflowMenu {
                        HintsDropdownItem(
                            isChecked = hintsOptionCheckedState,
                            onCheckedChange = { hintsOptionCheckedState = it }
                        )
                    }
                }

                AddRuleScreen(
                    uiState = AddRuleScreenUiState(
                        mutationType = mutationType,
                        showFormFieldHints = hintsOptionCheckedState,
                        showSaveButton = isCompactLayout,
                    ),
                    viewModel = viewModel,
                    onSaveClick = { handleSaveRuleClick() }
                )
            }
        }
    }
}

/**
 * Navigates to the given route with `launchSingleTop` enabled, to ensure that there will be at most
 * only one copy of a given destination on top of the back stack.
 */
fun NavHostController.navigateSingleTop(route: String, popUpToStartDestination: Boolean = true) {
    this.navigate(route) {
        // Pop up to the start destination of the graph to avoid building up a large stack of
        // destinations on the back stack as users select items
        if (popUpToStartDestination) {
            popUpTo(this@navigateSingleTop.graph.findStartDestination().id) {
                saveState = true
            }
        }

        // Avoid multiple copies of the same destination when re-selecting the same item
        launchSingleTop = true

        // Restore state when re-selecting a previously selected item
        restoreState = true
    }
}

/**
 * Instantiates a [ViewModel] shared between multiple destinations within a nested navigation graph,
 * scoped to the lifecycle of the parent entry.
 *
 * @param navController The app's [NavController].
 * @return A ViewModel that is an instance of the given [VM] type.
 */
@Composable
private inline fun <reified VM : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController
): VM {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    // Scope VM to nested nav graph parent entry to ensure that the VM won't be cleared if a single
    // screen within the nested nav graph is popped, but rather only if the whole nested nav graph
    // gets popped off the back stack
    return viewModel(parentEntry)
}