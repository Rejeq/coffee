package com.rejeq.sws.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rejeq.sws.R
import com.rejeq.sws.domain.entity.CoffeeShopMenu
import com.rejeq.sws.ui.theme.SwsTheme
import com.rejeq.sws.ui.utils.QuantitySlider
import com.rejeq.sws.ui.utils.toLocalizedPriceString

data class MenuItemState(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val price: Double,
    val quantity: Int,
)

fun CoffeeShopMenu.toMenuItemState(quantity: Int) = MenuItemState(
    id = id,
    title = name,
    imageUrl = imageURL,
    price = price,
    quantity = quantity,
)

@Composable
fun MenuItemContent(
    state: MenuItemState,
    modifier: Modifier = Modifier,
    onQuantityChange: (Int) -> Unit = {},
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
    ) {
        AsyncImage(
            model = state.imageUrl,
            contentDescription = stringResource(R.string.menu_img_desc),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            placeholder = painterResource(R.drawable.ic_image_24dp),
            error = painterResource(R.drawable.ic_image_24dp),
        )

        Column(
            modifier = Modifier.padding(
                top = 4.dp,
                start = 8.dp,
                end = 8.dp,
            ),
        ) {
            Text(
                text = state.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = state.price.toLocalizedPriceString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(end = 8.dp),
                )

                QuantitySlider(
                    quantity = state.quantity,
                    onQuantityChange = onQuantityChange,
                )
            }
        }
    }
}

@Composable
@Preview
@PreviewLightDark
@PreviewDynamicColors
private fun MenuItemPreview() {
    SwsTheme {
        Surface {
            MenuItemContent(
                state = MenuItemState(
                    id = 128,
                    title = "Menu Entry",
                    imageUrl = "https://picsum.photos/200",
                    price = 128.0,
                    quantity = 24,
                ),
            )
        }
    }
}
