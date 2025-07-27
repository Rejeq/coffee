package com.rejeq.sws.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rejeq.sws.R
import com.rejeq.sws.ui.menu.MenuViewModel.NavEvent
import com.rejeq.sws.ui.order.OrderState
import com.rejeq.sws.ui.theme.SwsTheme
import com.rejeq.sws.ui.utils.FailedContent
import com.rejeq.sws.ui.utils.LoadingContent
import com.rejeq.sws.ui.utils.NavEventSideEffect
import com.rejeq.sws.ui.utils.SwsButton
import com.rejeq.sws.ui.utils.SwsTopBar

@Composable
fun MenuContent(
    vm: MenuViewModel,
    modifier: Modifier = Modifier,
    onBackToLogin: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onOrderClick: (OrderState?) -> Unit = {},
) {
    NavEventSideEffect(vm.navigation) { event ->
        when (event) {
            NavEvent.BackToLogin -> onBackToLogin()
        }
    }

    MenuLayout(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SwsTopBar(
                title = stringResource(R.string.menu_top_bar_title),
                onBackClick = onBackClick,
            )
        },
        bottomButton = {
            SwsButton(
                onClick = {
                    onOrderClick(vm.getOrderState())
                },
                modifier = Modifier.fillMaxWidth(0.9f),
            ) {
                Text(text = stringResource(R.string.open_order_btn_title))
            }
        },
        content = {
            val state = vm.menuState.collectAsStateWithLifecycle().value

            when (state) {
                is MenuState.Error -> {
                    FailedContent(state.message)
                }
                is MenuState.Loading -> {
                    LoadingContent()
                }
                is MenuState.Success -> {
                    MenuListContent(
                        state.menu,
                        onChangeQuantity = vm::changeQuantity,
                    )
                }
            }
        },
    )
}

@Composable
fun MenuLayout(
    topBar: @Composable () -> Unit,
    bottomButton: @Composable () -> Unit,
    content: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        floatingActionButtonPosition = FabPosition.Center,
        topBar = topBar,
        floatingActionButton = bottomButton,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center,
            content = content,
        )
    }
}

@Composable
fun MenuListContent(
    state: List<MenuItemState>,
    modifier: Modifier = Modifier,
    onChangeQuantity: (MenuItemState, Int) -> Unit = { _, _ -> },
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxHeight().padding(horizontal = 16.dp),
        columns = GridCells.Adaptive(minSize = 156.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(state, key = { it.id }) { menuState ->
            MenuItemContent(
                state = menuState,
                onQuantityChange = {
                    onChangeQuantity(menuState, it)
                },
                modifier = Modifier.wrapContentSize(),
            )
        }
    }
}

@Composable
@Preview
@PreviewLightDark
@PreviewDynamicColors
@PreviewScreenSizes
private fun InfoContentPreview() {
    SwsTheme {
        MenuContent(
            vm = remember { PreviewMenuViewModel() },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
