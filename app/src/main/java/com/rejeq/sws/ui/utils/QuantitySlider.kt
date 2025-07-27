package com.rejeq.sws.ui.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.rejeq.sws.R
import com.rejeq.sws.ui.theme.SwsTheme

@Composable
fun QuantitySlider(
    quantity: Int,
    modifier: Modifier = Modifier,
    onQuantityChange: (Int) -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        MinusButton(onClick = { onQuantityChange(quantity - 1) })
        Text(text = quantity.toString())
        PlusButton(onClick = { onQuantityChange(quantity + 1) })
    }
}

@Composable
fun MinusButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(R.drawable.ic_remove_24dp),
            contentDescription = stringResource(R.string.remove_item_btn_desc),
            modifier = modifier,
        )
    }
}

@Composable
fun PlusButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(R.drawable.ic_add_24dp),
            contentDescription = stringResource(R.string.add_item_btn_desc),
            modifier = modifier,
        )
    }
}

@Composable
@Preview
@Preview(locale = "ar", name = "RTL")
@PreviewLightDark
@PreviewDynamicColors
private fun InfoContentPreview() {
    SwsTheme {
        Surface {
            QuantitySlider(
                quantity = 128,
            )
        }
    }
}
