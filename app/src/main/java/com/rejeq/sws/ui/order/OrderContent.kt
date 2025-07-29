package com.rejeq.sws.ui.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.rejeq.sws.R
import com.rejeq.sws.ui.theme.SwsTheme
import com.rejeq.sws.ui.utils.SwsButton
import com.rejeq.sws.ui.utils.SwsTopBar

@Composable
fun OrderContent(
    vm: OrderViewModel,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onPayClick: (OrderState) -> Unit = {},
) {
    MenuLayout(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SwsTopBar(
                title = stringResource(R.string.order_top_bar_title),
                onBackClick = onBackClick,
            )
        },
        bottomButton = {
            SwsButton(
                onClick = {
                    onPayClick(OrderState(vm.order.toList()))
                },
                modifier = Modifier.fillMaxWidth(0.9f),
            ) {
                Text(text = stringResource(R.string.open_payment_btn_title))
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(vm.order, key = { it.id }) { orderState ->
                    OrderItemContent(
                        state = orderState,
                        onQuantityChange = {
                            vm.changeQuantity(orderState, it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                item {
                    Text(
                        text = stringResource(R.string.order_notification),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(
                            vertical = 128.dp,
                        ).fillMaxWidth(),
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
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
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
        ) {
            content()
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
        OrderContent(
            vm = remember { PreviewOrderViewModel() },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
