package com.suvanl.fixmylinks.ui.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.SelectAll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.suvanl.fixmylinks.ui.screens.details.RuleDetailsScreen
import com.suvanl.fixmylinks.ui.screens.newruleflow.AddRuleScreen
import com.suvanl.fixmylinks.ui.screens.newruleflow.AddRuleScreenUiState
import com.suvanl.fixmylinks.ui.screens.newruleflow.RuleOptionsState
import com.suvanl.fixmylinks.ui.screens.newruleflow.SelectRuleTypeScreen
import com.suvanl.fixmylinks.ui.theme.TextStyleDefaults
import com.suvanl.fixmylinks.ui.util.getNewRuleFlowViewModel
import com.suvanl.fixmylinks.viewmodel.MainViewModel
import com.suvanl.fixmylinks.viewmodel.RulesViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.AddRuleViewModel
import com.suvanl.fixmylinks.viewmodel.newruleflow.SelectRuleTypeViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun FmlNavHost(
    navController: NavHostController,
    windowWidthSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
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

        composable(route = FmlScreen.Saved.route) {
            SavedScreen()
        }

        navigation(
            startDestination = FmlScreen.Rules.route,
            route = NestedNavGraphParent.RuleDetailsFlow.route
        ) {
            composable(route = FmlScreen.Rules.route) { navBackStackEntry ->
                val viewModel = navBackStackEntry.sharedViewModel<RulesViewModel>(navController)
                val uiState by viewModel.rulesScreenUiState.collectAsStateWithLifecycle()
                val selectedRules by mainViewModel.multiSelectedRules.collectAsStateWithLifecycle()

                val coroutineScope = rememberCoroutineScope()

                LaunchedEffect(key1 = Unit) {
                    mainViewModel.resetState()
                }

                ProvideAppBarActions {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                val allRulesSelected = selectedRules == uiState.rules.toSet()
                                if (!allRulesSelected) {
                                    selectedRules.forEach {
                                        viewModel.deleteSingleRule(it.baseRuleId)
                                    }
                                } else {
                                    viewModel.deleteAllRules()
                                }

                                // Clear selection
                                mainViewModel.updateMultiSelectedRules(setOf())
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.delete_selected)
                        )
                    }

                    IconButton(
                        onClick = {
                            // Add all rules to selected set
                            mainViewModel.updateMultiSelectedRules(uiState.rules.toSet())
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SelectAll,
                            contentDescription = stringResource(R.string.select_all)
                        )
                    }
                }

                RulesScreen(
                    uiState = uiState,
                    onClickRuleItem = { rule ->
                        coroutineScope.launch {
                            viewModel.setSelectedRule(rule)

                            // first non-null emission
                            val selectedRule = viewModel.selectedRule.filter { it != null }.first()

                            navController.navigateSingleTop(
                                route = "${FmlScreen.RuleDetails.route}/${selectedRule?.mutationType}/${selectedRule?.baseRuleId}",
                                popUpToStartDestination = false
                            )
                        }
                    },
                    selectedItems = selectedRules,
                    onUpdateSelectedItems = {
                        mainViewModel.updateMultiSelectedRules(it)
                    },
                )
            }

            composable(
                route = FmlScreen.RuleDetails.routeWithArgs,
                arguments = FmlScreen.RuleDetails.args
            ) { navBackStackEntry ->
                val viewModel = navBackStackEntry.sharedViewModel<RulesViewModel>(navController)
                val selectedRule by viewModel.selectedRule.collectAsStateWithLifecycle()
                val deleteConfirmationRequired by viewModel.deleteConfirmationRequired.collectAsStateWithLifecycle()

                val coroutineScope = rememberCoroutineScope()

                LaunchedEffect(key1 = Unit) {
                    viewModel.refreshSelectedRule()
                }

                ProvideAppBarActions {
                    OverflowMenu {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(id = R.string.delete),
                                    style = TextStyleDefaults.dropdownItemStyle,
                                )
                            },
                            onClick = {
                                if (selectedRule != null) {
                                    viewModel.setDeleteConfirmationRequired(true)
                                }
                            }
                        )
                    }
                }

                RuleDetailsScreen(
                    rule = selectedRule,
                    showDeleteConfirmation = deleteConfirmationRequired,
                    onDismissDeleteConfirmation = {
                        viewModel.setDeleteConfirmationRequired(false)
                    },
                    onDelete = {
                        coroutineScope.launch {
                            viewModel.deleteSingleRule(selectedRule!!.baseRuleId)
                            navController.popBackStack(
                                route = FmlScreen.Rules.route,
                                inclusive = false
                            )
                        }
                    }
                )
            }
        }

        navigation(
            startDestination = FmlScreen.SelectRuleType.route,
            route = NestedNavGraphParent.NewRuleFlow.route
        ) {
            composable(route = FmlScreen.SelectRuleType.route) {
                val viewModel = viewModel<SelectRuleTypeViewModel>()
                val mutationType by viewModel.mutationType.collectAsStateWithLifecycle()

                val isCompactLayout = windowWidthSize == WindowWidthSizeClass.Compact

                fun handleNextButtonClick() {
                    // The route used for adding a new rule:
                    //  - action is Action.ADD
                    //  - baseRuleId is 0 since one doesn't exist yet. A non-zero value should only
                    //    be passed as this argument if the action arg is Action.EDIT
                    val addActionRoute =
                        "${FmlScreen.AddRule.route}/${mutationType.name}/${FmlScreen.AddRule.Action.ADD}/${0L}"

                    navController.navigateSingleTop(
                        route = addActionRoute,
                        popUpToStartDestination = false
                    )
                }

                // Show "Next" button as top app bar action on Medium and Expanded layouts
                ProvideAppBarActions(
                    shouldShowActions = !isCompactLayout
                ) {
                    Button(
                        onClick = { handleNextButtonClick() }
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
                    onNextButtonClick = { handleNextButtonClick() }
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

                val actionArg = navBackStackEntry.arguments?.getString(FmlScreen.AddRule.actionArg)
                val action = FmlScreen.AddRule.Action.entries.find { it.name == actionArg }
                    ?: FmlScreen.AddRule.Action.ADD

                val baseRuleId =
                    navBackStackEntry.arguments?.getLong(FmlScreen.AddRule.baseRuleIdArg) ?: 0

                if (action == FmlScreen.AddRule.Action.EDIT && baseRuleId == 0L) {
                    throw IllegalStateException("base_rule_id nav arg cannot be 0 while action is Action.EDIT")
                }

                val viewModel: AddRuleViewModel = getNewRuleFlowViewModel(mutationType)
                val userPreferences by viewModel.userPreferences.collectAsStateWithLifecycle()

                val isCompactLayout = windowWidthSize == WindowWidthSizeClass.Compact
                var hintsOptionCheckedState by remember { mutableStateOf(true) }

                fun handleSaveRuleClick() {
                    if (!viewModel.validateData()) return

                    coroutineScope.launch {
                        val isEditing = action == FmlScreen.AddRule.Action.EDIT && baseRuleId != 0L

                        if (isEditing) {
                            viewModel.updateExistingRule(baseRuleId)
                        } else {
                            viewModel.saveRule()
                        }

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
                            isChecked = userPreferences.showFormFieldHints,
                            onCheckedChange = { isChecked ->
                                coroutineScope.launch {
                                    viewModel.updateShowHintsPreference(isChecked)
                                    hintsOptionCheckedState = userPreferences.showFormFieldHints
                                }
                            }
                        )
                    }
                }

                AddRuleScreen(
                    uiState = AddRuleScreenUiState(
                        mutationType = mutationType,
                        showFormFieldHints = userPreferences.showFormFieldHints,
                        showSaveButton = isCompactLayout,
                        ruleOptions = RuleOptionsState(),
                    ),
                    viewModel = viewModel,
                    onSaveClick = { handleSaveRuleClick() },
                    action = action,
                    baseRuleId = baseRuleId,
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
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    // Scope VM to nested nav graph parent entry to ensure that the VM won't be cleared if a single
    // screen within the nested nav graph is popped, but rather only if the whole nested nav graph
    // gets popped off the back stack
    return hiltViewModel(parentEntry)
}