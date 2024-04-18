package com.example.gym

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gym.home.HomeScreen
import com.example.gym.nav.Back
import com.example.gym.nav.BackTo
import com.example.gym.nav.CleanForward
import com.example.gym.nav.Forward
import com.example.gym.nav.NavigationDirections
import com.example.gym.nav.NavigationManager
import com.example.gym.nav.Replace
import com.example.gym.nav.ShowMessage
import com.example.gym.new_session.NewSessionScreen
import com.example.gym.ui.theme.GymTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var navigationManager: NavigationManager

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted
                // You can now handle the notification logic here
            } else {
                // Permission is denied
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        setContent {
            GymTheme {
                val navController = rememberNavController()

                var showBanner by remember {
                    mutableStateOf(false)
                }
                var messageType: ShowMessage.MessageType by remember {
                    mutableStateOf(ShowMessage.MessageType.Positive)
                }
                var message: String by remember {
                    mutableStateOf("")
                }
                LaunchedEffect(showBanner) {
                    if (showBanner) {
                        delay(3000L)
                        showBanner = false
                    }
                }
                LaunchedEffect(Unit) {
                    navigationManager.commands.onEach { command ->
                        if (navController.currentDestination?.route != command.directions.destination) {
                            try {
                                when (command) {
                                    is Forward -> navController.navigate(command.directions.destination) {
                                        launchSingleTop = command.singleTop
                                    }

                                    is CleanForward -> {
                                        navController.clearBackStack()
                                        navController.navigate(command.directions.destination) {
                                            navController.clearSelf(this)
                                            launchSingleTop = command.singleTop
                                        }
                                    }

                                    is Replace ->
                                        navController.navigate(command.directions.destination) {
                                            navController.clearSelf(this)
                                            launchSingleTop = command.singleTop
                                        }

                                    is Back -> {
                                        if (navController.previousBackStackEntry != null) {
                                            navController.popBackStack()
                                        }
                                    }

                                    is BackTo ->
                                        navController.popBackStack(
                                            route = command.directions.destination,
                                            inclusive = false
                                        )

                                    is ShowMessage -> {
                                        message = command.message.asString(this@MainActivity)
                                        messageType = command.messageType
                                        showBanner = true
                                    }
                                    else -> {

                                    }
                                }
                            } catch (e: Exception) {

                            }
                        }
                    }.launchIn(this)
                }

                if (showBanner) {
                    Banner(
                        messageType,
                        message
                    ) { showBanner = false }
                }

                var showBottomNavigation by remember {
                    mutableStateOf(true)
                }

                navController.addOnDestinationChangedListener { _, destination, _ ->
                    showBottomNavigation = when (destination.route) {
                        else -> true
                    }
                }

                Scaffold(
                    contentColor = Color.White,
                    containerColor = Color.White
                ) { padding ->
                    NavHost(
                        modifier = Modifier.padding(padding),
                        navController = navController,
                        startDestination = NavigationDirections.Home.destination,
                    ) {
                        composable(route = NavigationDirections.Home.destination) {
                            HomeScreen()
                        }
                        composable(route = NavigationDirections.History.destination) {
                            HomeScreen()
                        }
                        composable(route = NavigationDirections.Progress.destination) {
                            HomeScreen()
                        }
                        composable(route = NavigationDirections.Session.destination) {
                            HomeScreen()
                        }
                        composable(route = NavigationDirections.NewSession.destination) {
                            NewSessionScreen()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Banner(
        messageType: ShowMessage.MessageType,
        message: String,
        onDismissRequest: () -> Unit
    ) {
        val bannerStyle = messageType.getBannerStyle()
        Popup(
            alignment = Alignment.TopCenter,
            offset = IntOffset(0, 48),
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        ) {
            Surface(
                color = bannerStyle.backgroundColor,
                contentColor = bannerStyle.backgroundColor,
                border = BorderStroke(1.dp, bannerStyle.strokeColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(vertical = 16.dp, horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Image(
                        painter = painterResource(id = bannerStyle.resId),
                        contentDescription = null
                    )
                    Text(
                        text = message,
                        style = GymTheme.typography.bodyLarge.copy(
                            fontSize = 18.sp,
                            lineHeight = 23.4.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF000000),
                        )
                    )
                }
            }
        }
    }

    private fun NavHostController.clearBackStack() {
        while (previousBackStackEntry != null) {
            popBackStack()
        }
    }

    private fun NavHostController.clearSelf(options: NavOptionsBuilder) {
        currentDestination?.route?.let {
            options.popUpTo(it) { inclusive = true }
        }
    }
}