package com.example.film_catalog_android.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.film_catalog_android.core.ui.BottomNavigationBar
import com.example.film_catalog_android.presentation.details.DetailsScreen
import com.example.film_catalog_android.presentation.home.HomeScreen
import com.example.film_catalog_android.presentation.profile.ProfileScreen
import com.example.film_catalog_android.presentation.search.SearchScreen
import com.example.film_catalog_android.presentation.profile.settings.SettingsScreen
import com.example.film_catalog_android.presentation.auth.LoginScreen
import com.example.film_catalog_android.presentation.auth.RegisterScreen
import com.example.film_catalog_android.presentation.auth.AuthStartState
import com.example.film_catalog_android.presentation.auth.AuthStartViewModel
import com.example.film_catalog_android.presentation.admin.AddMovieScreen
import com.example.film_catalog_android.presentation.admin.EditMovieScreen
import com.example.film_catalog_android.presentation.admin.ManageMoviesScreen

@Composable
fun AppNavigation(
    authStartViewModel: AuthStartViewModel = viewModel()
) {
    val authStartState by authStartViewModel.authStartState.collectAsState()

    if (authStartState == AuthStartState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val startDestination = when (authStartState) {
        AuthStartState.Authorized -> Screen.Home.route
        AuthStartState.Unauthorized -> Screen.Login.route
        AuthStartState.Loading -> Screen.Login.route
    }

    val bottomBarRoutes = listOf(
        Screen.Search.route,
        Screen.Home.route,
        Screen.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (currentDestination?.route in bottomBarRoutes) {
                BottomNavigationBar(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    },
                    onRegisterClick = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    },
                    onLoginClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Search.route) {
                SearchScreen(
                    onMovieClick = { movieId ->
                        navController.navigate(Screen.Details.createRoute(movieId))
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    onMovieClick = { movieId ->
                        navController.navigate(Screen.Details.createRoute(movieId))
                    },
                    onAddMovieClick = {
                        navController.navigate(Screen.AddMovie.route)
                    },
                    onManageMoviesClick = {
                        navController.navigate(Screen.ManageMovies.route)
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onMovieClick = { movieId ->
                        navController.navigate(Screen.Details.createRoute(movieId))
                    },
                    onSettingsClick = {
                        navController.navigate(Screen.Settings.route)
                    }
                )
            }

            composable(
                route = Screen.Details.route,
                arguments = listOf(
                    navArgument("movieId") {
                        type = NavType.StringType
                    }
                )
            ) {
                DetailsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onLogoutClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.ManageMovies.route) {
                ManageMoviesScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onEditMovieClick = { movieId ->
                        navController.navigate(Screen.EditMovie.createRoute(movieId))
                    }
                )
            }

            composable(Screen.AddMovie.route) {
                AddMovieScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onMovieAdded = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screen.EditMovie.route,
                arguments = listOf(
                    navArgument("movieId") {
                        type = NavType.StringType
                    }
                )
            ) {
                EditMovieScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onMovieSaved = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
