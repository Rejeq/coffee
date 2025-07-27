package com.rejeq.sws.ui.utils

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.rejeq.sws.ui.theme.SwsTheme

@Composable
fun SwsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        content = content,
    )
}

@Composable
@Preview
@PreviewLightDark
@PreviewDynamicColors
fun PreviewSwsButton() {
    SwsTheme {
        SwsButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            content = {
                Text("Something")
            },
        )
    }
}
