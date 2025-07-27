package com.rejeq.sws.ui.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rejeq.sws.R
import com.rejeq.sws.ui.utils.SwsTopBar
import ru.sulgik.mapkit.compose.TitledPlacemark
import ru.sulgik.mapkit.compose.YandexMap
import ru.sulgik.mapkit.compose.YandexMapsComposeExperimentalApi
import ru.sulgik.mapkit.compose.bindToLifecycleOwner
import ru.sulgik.mapkit.compose.imageProvider
import ru.sulgik.mapkit.compose.rememberAndInitializeMapKit
import ru.sulgik.mapkit.compose.rememberCameraPositionState
import ru.sulgik.mapkit.compose.rememberPlacemarkState
import ru.sulgik.mapkit.geometry.Point
import ru.sulgik.mapkit.map.TextStyle

@OptIn(YandexMapsComposeExperimentalApi::class)
@Composable
fun MapContent(
    vm: MapViewModel,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onShopClick: (Long) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SwsTopBar(
                title = stringResource(R.string.map_top_bar_title),
                onBackClick = onBackClick,
            )
        },
    ) { padding ->
        rememberAndInitializeMapKit().bindToLifecycleOwner()

        val cameraPositionState = rememberCameraPositionState()

        YandexMap(
            cameraPositionState = cameraPositionState,
            modifier = Modifier.padding(padding).fillMaxSize(),
        ) {
            val coffeeIcon = imageProvider(
                size = DpSize(20.dp, 20.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_coffee),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )
            }

            val marksState = vm.marksState.collectAsStateWithLifecycle().value
            if (marksState is MarksState.Success) {
                marksState.marks.forEach { mark ->
                    val geometry =
                        Point(mark.point.latitude, mark.point.longitude)

                    TitledPlacemark(
                        state = rememberPlacemarkState(geometry),
                        title = mark.name,
                        icon = coffeeIcon,
                        onTap = {
                            onShopClick(mark.id)
                            true
                        },
                        titleStyle = TextStyle(
                            placement = TextStyle.Placement.BOTTOM,
                        ),
                    )
                }
            }
        }
    }
}
