package com.rejeq.sws.ui.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.rejeq.sws.ui.theme.SwsTheme
import com.rejeq.sws.ui.utils.QuantitySlider
import com.rejeq.sws.ui.utils.toLocalizedPriceString

@Composable
fun OrderItemContent(
    state: OrderItemState,
    modifier: Modifier = Modifier,
    onQuantityChange: (Int) -> Unit = {},
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = state.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(text = state.price.toLocalizedPriceString())
            }

            QuantitySlider(
                quantity = state.quantity,
                onQuantityChange = onQuantityChange,
            )
        }
    }
}

@Composable
@Preview
@PreviewLightDark
@PreviewDynamicColors
fun OrderItemContentPreview() {
    SwsTheme {
        OrderItemContent(
            state = OrderItemState(
                id = 128,
                name = "Order Entry",
                price = 128.0,
                quantity = 24,
            ),
        )
    }
}
