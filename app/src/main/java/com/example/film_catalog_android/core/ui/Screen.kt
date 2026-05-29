package com.example.film_catalog_android.core.navigation

sealed class Screen(
    val route: String,
    val title: String
) {
    data object Search : Screen(
        route = "search",
        title = "Поиск"
    )

    data object Home : Screen(
        route = "home",
        title = "Главная"
    )

    data object Profile : Screen(
        route = "profile",
        title = "Профиль"
    )

    data object Details : Screen(
        route = "details/{movieId}",
        title = "Детали"
    ) {
        fun createRoute(movieId: Long): String {
            return "details/$movieId"
        }
    }

    data object Settings : Screen(
        route = "settings",
        title = "Настройки"
    )

    data object Login : Screen(
        route = "login",
        title = "Авторизация"
    )

    data object Register : Screen(
        route = "register",
        title = "Регистрация"
    )

    //Админская часть
    data object ManageMovies : Screen(
        route = "manage_movies",
        title = "Управление фильмами"
    )

    data object AddMovie : Screen(
        route = "add_movie",
        title = "Добавление фильма"
    )

    data object EditMovie : Screen(
        route = "edit_movie/{movieId}",
        title = "Изменение фильма"
    ) {
        fun createRoute(movieId: Long): String {
            return "edit_movie/$movieId"
        }
    }
}