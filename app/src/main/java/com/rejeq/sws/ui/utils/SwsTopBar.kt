package com.rejeq.sws.ui.utils

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.rejeq.sws.R
import com.rejeq.sws.ui.theme.SwsTheme

@Composable
fun SwsTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Box(modifier = modifier) {
        RawTopBar(
            title = title,
            onBackClick = onBackClick,
            actions = actions,
        )

        TwoLines(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RawTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            onBackClick?.let {
                IconButton(onClick = it) {
                    val painter = painterResource(R.drawable.ic_arrow_back_24dp)
                    val desc = stringResource(R.string.back_btn_desc)

                    Icon(painter, desc)
                }
            }
        },
        actions = actions,
    )
}

@Composable
fun TwoLines(modifier: Modifier = Modifier) {
    val lineColor = MaterialTheme.colorScheme.surfaceTint

    Canvas(
        modifier = modifier.height(2.dp),
    ) {
        drawLine(
            color = lineColor,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 1f,
        )
        drawLine(
            color = lineColor,
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = 1f,
        )
    }
}

@Composable
@Preview
@PreviewLightDark
@PreviewDynamicColors
private fun PreviewSwsTopBar() {
    SwsTheme {
        SwsTopBar(
            title = "Title",
            onBackClick = {},
        )
    }
}

@Composable
@Preview
@PreviewLightDark
@PreviewDynamicColors
private fun PreviewSwsTopBarWithoutBackButton() {
    SwsTheme {
        SwsTopBar(
            title = "Title",
        )
    }
}

@Composable
@Preview
@PreviewLightDark
@PreviewDynamicColors
private fun PreviewSwsTopBarWithLongTitle() {
    SwsTheme {
        SwsTopBar(
            title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit," +
                "sed do eiusmod tempor incididunt ut labore et dolore magna",
            onBackClick = {},
        )
    }
}
