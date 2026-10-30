package com.viaplay.test

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.viaplay.test.common.ui.theme.AppTheme
import com.viaplay.test.feature.dashboard.DashboardScreen
import com.viaplay.test.feature.details.DetailsScreen
import com.viaplay.test.feature.details.SectionViewModel
import com.viaplay.test.navigation.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    AppNavGraph()
                }
            }
        }
    }
}


@Composable
fun AppNavGraph() {
    val navState = rememberNavigationState(
        startRoute = Routes.LinksRoute,
        topLevelRoutes = setOf(Routes.LinksRoute)
    )
    val navigator = remember(navState) { AppNavigator(navState) }

    NavDisplay(
        entries = navState.toEntries { key ->
            when (key) {
                is Routes.LinksRoute -> NavEntry(key) {
                    DashboardScreen(
                        hiltViewModel(),
                        navigator::navigate
                    )
                }

                is Routes.DetailsRoute -> NavEntry(key) {
                    DetailsScreen(
                        hiltViewModel<SectionViewModel, SectionViewModel.Factory>(
                            key = "Details_${key.link.id}",
                            creationCallback = { factory -> factory.create(key.link) },
                        ),
                        navigator::goBack
                    )
                }

                else -> error("Unknown route: $key")
            }
        },
        onBack = navigator::goBack
    )
}