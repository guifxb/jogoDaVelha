package com.gfbdev.jogoDaVelha.presentation.navigation

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.gfbdev.jogoDaVelha.R
import com.gfbdev.jogoDaVelha.presentation.ads.AdManager
import com.gfbdev.jogoDaVelha.presentation.screens.GameScreen
import com.gfbdev.jogoDaVelha.presentation.screens.PastGamesScreen
import com.gfbdev.jogoDaVelha.presentation.screens.SplashScreen
import com.gfbdev.jogoDaVelha.presentation.screens.StartScreen
import com.gfbdev.jogoDaVelha.presentation.viewModel.GameResult
import com.gfbdev.jogoDaVelha.presentation.viewModel.GameViewModel
import com.gfbdev.jogoDaVelha.ui.theme.LogoBlue
import org.koin.androidx.compose.getViewModel

enum class AppScreen(val title: String) {
    SPLASH(""),
    START("Jogo da Velha"),
    GAME("Partida"),
    PAST_GAMES("Histórico de Partidas"),
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val isSplash = currentRoute == AppScreen.SPLASH.name

    val activity = LocalContext.current as ComponentActivity
    val adManager = remember { AdManager(activity) }
    val gameViewModel = getViewModel<GameViewModel>()
    val uiState by gameViewModel.uiState.collectAsState()

    val canPop by remember {
        derivedStateOf { navController.previousBackStackEntry != null }
    }

    LaunchedEffect(isSplash) {
        val barColor = if (isSplash) LogoBlue.toArgb() else android.graphics.Color.TRANSPARENT
        activity.enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
            navigationBarStyle = if (isSplash) {
                SystemBarStyle.dark(barColor)
            } else {
                SystemBarStyle.auto(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = if (isSplash) LogoBlue else MaterialTheme.colorScheme.background,
            topBar = {
                if (!isSplash) {
                    val screen = AppScreen.entries.find { it.name == currentRoute } ?: AppScreen.START
                    AppBar(
                        currentScreen = screen,
                        canNavigateBack = canPop,
                        navigateUp = { adManager.showInterstitialAd { navController.popBackStack() } }
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = AppScreen.SPLASH.name,
                modifier = Modifier.padding(if (isSplash) PaddingValues() else paddingValues),
                enterTransition = { fadeIn(animationSpec = tween(700)) },
                exitTransition = { fadeOut(animationSpec = tween(700)) }
            ) {
                composable(route = AppScreen.SPLASH.name) {
                    SplashScreen(onAnimationFinished = {
                        navController.navigate(AppScreen.START.name) {
                            popUpTo(AppScreen.SPLASH.name) { inclusive = true }
                        }
                    })
                }
                composable(route = AppScreen.START.name) {
                    StartScreen(
                        player1State = gameViewModel.player1State.value,
                        onPlayer1Changed = gameViewModel::updatePlayer1State,
                        player2State = gameViewModel.player2State.value,
                        onPlayer2Changed = gameViewModel::updatePlayer2State,
                        onGameClicked = { boardSize ->
                            gameViewModel.updateBoardSize(boardSize)
                            navController.navigate(AppScreen.GAME.name)
                            gameViewModel.startGame()
                        },
                        onHistoryClicked = { navController.navigate(AppScreen.PAST_GAMES.name) },
                        isCPU = gameViewModel.isCPU.value,
                        onCPUChanged = gameViewModel::updateIsCPU
                    )
                }
                composable(route = AppScreen.GAME.name) {
                    GameScreen(
                        uiState = uiState,
                        onMakeMove = { row, col -> gameViewModel.makeMove(row, col) },
                        onRestartClicked = { gameViewModel.startGame() }
                    )
                }
                composable(route = AppScreen.PAST_GAMES.name) {
                    PastGamesScreen(
                        pastPlays = gameViewModel.pastPlays.collectAsState().value.reversed(),
                        onDeleteAll = { gameViewModel.deleteAll() }
                    )
                }
            }
        }

        if (uiState.gameResult != GameResult.NONE && uiState.gameResult != GameResult.DRAW) {
            FullScreenResultAnimation(
                result = uiState.gameResult,
                onFinished = { gameViewModel.resetGameResult() }
            )
        }
    }

    BackHandler(enabled = canPop) {
        adManager.showInterstitialAd { navController.popBackStack() }
    }
}

@Composable
fun FullScreenResultAnimation(result: GameResult, onFinished: () -> Unit) {
    val animRes = if (result == GameResult.WIN) R.raw.win_anim else R.raw.lose_anim
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animRes))
    val progress by animateLottieCompositionAsState(composition)

    if (progress == 1f) {
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onFinished() },
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    currentScreen: AppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { Text(currentScreen.title) },
        modifier = Modifier.background(MaterialTheme.colorScheme.primary),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                }
            }
        }
    )
}
