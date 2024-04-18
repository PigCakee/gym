package com.example.gym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavGraph.Companion.findStartDestination
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
import com.example.gym.ui.theme.BottomGradient
import com.example.gym.ui.theme.GymTheme
import com.example.gym.ui.theme.Mono400
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var navigationManager: NavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    bottomBar = {
                        BottomNavigation(navController, showBottomNavigation)
                    },
                    contentColor = Color.White,
                    containerColor = Color.White
                ) { padding ->
                    NavHost(
                        modifier = Modifier.padding(padding),
                        navController = navController,
                        startDestination = NavigationDirections.Home.destination
                    ) {
                        composable(route = NavigationDirections.Home.destination) {
                            HomeScreen()
                        }
                        composable(route = NavigationDirections.History.destination) {

                        }
                        composable(route = NavigationDirections.Progress.destination) {

                        }
                        composable(route = NavigationDirections.Session.destination) {

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

    @Composable
    private fun BottomNavigation(
        navController: NavHostController,
        showBottomNavigation: Boolean
    ) {
        var navigationSelectedItem by remember {
            mutableIntStateOf(0)
        }
        val items = NavigationDirections.getBottomNavigationItems()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            items.forEachIndexed { index, item ->
                if (item.destination == destination.route) {
                    navigationSelectedItem = index
                }
            }
        }
        AnimatedVisibility(
            visible = showBottomNavigation,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            Surface(
                shadowElevation = 8.dp
            ) {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier.height(60.dp),
                ) {
                    items
                        .forEachIndexed { index, item ->
                            val selected = navigationSelectedItem == index
                            NavigationBarItem(
                                alwaysShowLabel = false,
                                modifier = Modifier.size(48.dp),
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color.Transparent,
                                    selectedIconColor = GymTheme.colors.primary,
                                    unselectedIconColor = Mono400
                                ),
                                interactionSource = object : MutableInteractionSource {
                                    override val interactions: Flow<Interaction>
                                        get() = flow { }

                                    override suspend fun emit(interaction: Interaction) {

                                    }

                                    override fun tryEmit(interaction: Interaction): Boolean {
                                        return false
                                    }
                                },
                                selected = selected,
                                onClick = {
                                    navigationSelectedItem = index
                                    navController.navigate(item.destination) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Surface(
                                        color = Color.Transparent,
                                        contentColor = Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .background(
                                                    brush = if (selected) BottomGradient else Brush.horizontalGradient(
                                                        listOf(
                                                            Color.Transparent,
                                                            Color.Transparent
                                                        )
                                                    ),
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .clickable {
                                                    navigationSelectedItem = index
                                                    navController.navigate(item.destination) {
                                                        popUpTo(navController.graph.findStartDestination().id) {
                                                            saveState = true
                                                        }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                },
                                        ) {
                                            Icon(
                                                modifier = Modifier.align(Alignment.Center),
                                                painter = item.icon,
                                                contentDescription = null,
                                                tint = if (selected) GymTheme.colors.primary else GymTheme.colors.textSecondary
                                            )
                                        }
                                    }
                                }
                            )
                        }
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