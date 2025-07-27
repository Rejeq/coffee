package com.rejeq.sws.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rejeq.sws.BuildConfig
import com.rejeq.sws.ui.root.RootContent
import com.rejeq.sws.ui.root.RootViewModel
import com.rejeq.sws.ui.theme.SwsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import ru.sulgik.mapkit.MapKit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var vm: RootViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        MapKit.setApiKey(BuildConfig.YANDEX_MAP_API_KEY)

        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            !vm.wannaShowScreen()
        }

        setContent {
            SwsTheme {
                RootContent(vm = vm)
            }
        }
    }
}
