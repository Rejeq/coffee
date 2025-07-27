package com.rejeq.sws.ui.shops

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.rejeq.sws.R
import com.rejeq.sws.domain.entity.Distance
import com.rejeq.sws.domain.entity.NearShop
import com.rejeq.sws.ui.theme.SwsTheme

@Composable
fun ShopItem(
    state: NearShop,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Text(
                text = state.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = state.distance?.toLocalizedString() ?: "",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}

@Composable
fun Distance.toLocalizedString(): String = when {
    this.value >= 1000 -> {
        val km = (this.value / 1000).toInt()
        pluralStringResource(R.plurals.distance_kilometers, km, km)
    }
    else -> {
        val meters = this.value.toInt()
        pluralStringResource(R.plurals.distance_meters, meters, meters)
    }
}

@Composable
@Preview
@PreviewLightDark
@PreviewDynamicColors
private fun ShopContentPreview() {
    SwsTheme {
        ShopItem(
            state = NearShop(123, "Shop", Distance(3000.0)),
        )
    }
}
