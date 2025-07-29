package com.rejeq.sws.ui.shops

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rejeq.sws.R
import com.rejeq.sws.domain.entity.NearShop
import com.rejeq.sws.domain.usecase.hasPermission
import com.rejeq.sws.ui.map.MapPlacemark
import com.rejeq.sws.ui.shops.ShopsViewModel.NavEvent
import com.rejeq.sws.ui.theme.SwsTheme
import com.rejeq.sws.ui.utils.FailedContent
import com.rejeq.sws.ui.utils.LoadingContent
import com.rejeq.sws.ui.utils.NavEventSideEffect
import com.rejeq.sws.ui.utils.SwsButton
import com.rejeq.sws.ui.utils.SwsTopBar

@Composable
fun ShopsContent(
    vm: ShopsViewModel,
    modifier: Modifier = Modifier,
    onBackToLogin: () -> Unit = {},
    onMapClick: () -> Unit = {},
    onShopClick: (Long) -> Unit = {},
) {
    RequestPermissionSideEffect(Manifest.permission.ACCESS_FINE_LOCATION) {
        if (it) {
            vm.onLocationRequested()
        }
    }

    NavEventSideEffect(vm.navigation) { event ->
        when (event) {
            NavEvent.BackToLogin -> onBackToLogin()
        }
    }

    ShopsLayout(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SwsTopBar(
                title = stringResource(R.string.shops_top_bar_title),
                onBackClick = onBackToLogin,
            )
        },

        bottomButton = {
            SwsButton(
                onClick = onMapClick,
                modifier = Modifier.fillMaxWidth(0.9f),
            ) {
                Text(text = stringResource(R.string.open_on_map_btn_title))
            }
        },

        content = {
            val state = vm.shopsState.collectAsStateWithLifecycle().value

            when (state) {
                is ShopsState.Error -> {
                    FailedContent(state.message)
                }
                is ShopsState.Loading -> {
                    LoadingContent()
                }
                is ShopsState.Success -> {
                    ShopsListContent(
                        state.shops,
                        onShopClick = onShopClick,
                    )
                }
            }
        },
    )
}

@Composable
fun ShopsListContent(
    shops: List<NearShop>,
    modifier: Modifier = Modifier,
    onShopClick: (Long) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp),
    ) {
        items(shops, key = { it.id }) { shopState ->
            ShopItem(
                state = shopState,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onShopClick(shopState.id) },
            )
        }
    }
}

@Composable
fun ShopsLayout(
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
            contentAlignment = Alignment.TopStart,
        ) {
            content()
        }
    }
}

@Composable
fun RequestPermissionSideEffect(
    permission: String,
    onPermissionResult: (Boolean) -> Unit,
) {
    val context = LocalContext.current

    val hasLocationPermission = context.hasPermission(
        permission,
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        onPermissionResult(isGranted)
    }

    LaunchedEffect(hasLocationPermission, onPermissionResult) {
        if (!hasLocationPermission) {
            launcher.launch(permission)
        } else {
            onPermissionResult(true)
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
        ShopsContent(
            vm = remember { PreviewShopsViewModel() },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
