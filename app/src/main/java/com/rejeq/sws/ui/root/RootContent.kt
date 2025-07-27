package com.rejeq.sws.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rejeq.sws.ui.login.DefaultLoginViewModel
import com.rejeq.sws.ui.login.LoginContent
import com.rejeq.sws.ui.map.DefaultMapViewModel
import com.rejeq.sws.ui.map.MapContent
import com.rejeq.sws.ui.menu.DefaultMenuViewModel
import com.rejeq.sws.ui.menu.MenuContent
import com.rejeq.sws.ui.order.DefaultOrderViewModel
import com.rejeq.sws.ui.order.OrderContent
import com.rejeq.sws.ui.order.OrderState
import com.rejeq.sws.ui.register.DefaultRegisterViewModel
import com.rejeq.sws.ui.register.RegisterContent
import com.rejeq.sws.ui.shops.DefaultShopsViewModel
import com.rejeq.sws.ui.shops.ShopsContent

@Composable
fun RootContent(vm: RootViewModel, modifier: Modifier = Modifier) {
    val navState = vm.navState.collectAsStateWithLifecycle().value

    when (navState) {
        is NavigationState.Loading -> {
        }
        is NavigationState.Success -> {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = navState.startDest,
                modifier = modifier,
            ) {
                routeRegister(navController)
                routeLogin(navController)
                routeShops(navController)
                routeMenu(navController)
                routeOrder(navController)
                routeMap(navController)
            }
        }
    }
}

private fun NavGraphBuilder.routeRegister(navController: NavHostController) {
    composable<RegisterRoute> {
        RegisterContent(
            vm = hiltViewModel<DefaultRegisterViewModel>(),
            onOpenShops = { navController.navigate(ShopsRoute) },
        )
    }
}

private fun NavGraphBuilder.routeLogin(navController: NavHostController) {
    composable<LoginRoute> {
        LoginContent(
            vm = hiltViewModel<DefaultLoginViewModel>(),
            onOpenShops = { navController.navigate(ShopsRoute) },
        )
    }
}

private fun NavGraphBuilder.routeShops(navController: NavHostController) {
    composable<ShopsRoute> {
        ShopsContent(
            vm = hiltViewModel<DefaultShopsViewModel>(),
            onBackToLogin = { onBackToLogin(navController) },
            onMapClick = { navController.navigate(MapRoute) },
            onShopClick = { id ->
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("id", id)
                navController.navigate(MenuRoute)
            },
        )
    }
}

private fun NavGraphBuilder.routeMenu(navController: NavHostController) {
    composable<MenuRoute> { backStack ->
        MenuContent(
            vm = hiltViewModel { factory: DefaultMenuViewModel.Factory ->
                val id = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Long>("id")

                factory.create(id ?: 0)
            },
            onBackClick = { navController.popBackStack() },
            onBackToLogin = { onBackToLogin(navController) },
            onOrderClick = { order ->
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("order", order)

                navController.navigate(OrderRoute)
            },
        )
    }
}

private fun NavGraphBuilder.routeOrder(navController: NavHostController) {
    composable<OrderRoute> {
        OrderContent(
            vm = hiltViewModel { factory: DefaultOrderViewModel.Factory ->
                val order = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<OrderState>("order")

                factory.create(order ?: OrderState(emptyList()))
            },
            onBackClick = { navController.popBackStack() },
            onPayClick = {},
        )
    }
}

private fun NavGraphBuilder.routeMap(navController: NavHostController) {
    composable<MapRoute> {
        MapContent(
            vm = hiltViewModel<DefaultMapViewModel>(),
            onBackClick = { navController.popBackStack() },
            onShopClick = { id ->
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("id", id)
                navController.navigate(MenuRoute)
            },
        )
    }
}

private fun onBackToLogin(navController: NavHostController) {
    navController.navigate(LoginRoute) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}
