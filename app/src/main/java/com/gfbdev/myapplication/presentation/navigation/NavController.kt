package com.gfbdev.myapplication.presentation.navigation

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gfbdev.myapplication.presentation.screens.GameScreen
import com.gfbdev.myapplication.presentation.screens.PastGamesScreen
import com.gfbdev.myapplication.presentation.screens.StartScreen
import com.gfbdev.myapplication.presentation.viewModel.GameViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import org.koin.androidx.compose.getViewModel
import java.util.concurrent.TimeUnit


enum class AppScreen(val title: String) {
    START("Jogo da Velha"), GAME("Partida"), PAST_GAMES("Histórico de Partidas"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavController(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val activity = LocalContext.current as Activity
    var isAdLoading by remember { mutableStateOf(false) }
    var lastAdShownTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val adLoadInterval = TimeUnit.MINUTES.toMillis(2)
    val canNavigateBack = navController.previousBackStackEntry != null

    val currentScreen =
        AppScreen.valueOf(backStackEntry?.destination?.route ?: AppScreen.START.name)

    val gameViewModel = getViewModel<GameViewModel>()

    fun loadInterstitialAd(activity: Activity, callback: (InterstitialAd?) -> Unit) {
        isAdLoading = true
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(activity, "ca-app-pub-3940256099942544/1033173712",
            adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.toString())
                    isAdLoading = false
                    callback(null)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    isAdLoading = false
                    callback(interstitialAd)
                }
            })
    }

    fun showInterstitialAd(navigateCallback: () -> Unit) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAdShownTime >= adLoadInterval && !isAdLoading) {
            isAdLoading = true
            loadInterstitialAd(activity) { interstitialAd ->
                isAdLoading = false
                if (interstitialAd != null) {
                    interstitialAd.show(activity)
                    interstitialAd.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                lastAdShownTime = System.currentTimeMillis()
                                navigateCallback()
                            }
                        }
                } else {
                    navigateCallback()
                }
            }
        } else {
            navigateCallback()
        }
    }

    Scaffold(modifier.fillMaxSize(), topBar = {
        AppBar(currentScreen = currentScreen,
            canNavigateBack = canNavigateBack,
            navigateUp = { showInterstitialAd { navController.popBackStack() } })
    }) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (isAdLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                NavHost(
                    navController = navController,
                    startDestination = AppScreen.START.name,
                ) {
                    composable(route = AppScreen.START.name) {
                        StartScreen(onGameClicked = { boardSize ->
                            gameViewModel.updateBoardSize(boardSize)
                            navController.navigate(AppScreen.GAME.name)
                            gameViewModel.startGame()
                        },
                            onHistoryClicked = {
                                navController.navigate(AppScreen.PAST_GAMES.name)
                            },
                            player1State = gameViewModel.player1State.value,
                            onPlayer1Changed = gameViewModel::updatePlayer1State,
                            player2State = gameViewModel.player2State.value,
                            onPlayer2Changed = gameViewModel::updatePlayer2State,
                            isCPU = gameViewModel.isCPU.value,
                            onCPUChanged = gameViewModel::updateIsCPU
                        )
                    }

                    composable(route = AppScreen.GAME.name) {
                        GameScreen(
                            modifier = modifier,
                            currentGame = gameViewModel.jogoDaVelha.value,
                            gameState = gameViewModel.gameStatus.value,
                            onMakeMove = { row, col ->
                                gameViewModel.makeMove(row, col)
                            },
                            onRestartClicked = { gameViewModel.startGame() },
                        )
                    }

                    composable(route = AppScreen.PAST_GAMES.name) {
                        PastGamesScreen(modifier = modifier,
                            pastPlays = gameViewModel.pastPlays.collectAsState().value.reversed(),
                            onDeleteAll = { gameViewModel.deleteAll() })
                    }
                }
                BackHandler(enabled = canNavigateBack) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastAdShownTime >= adLoadInterval && !isAdLoading) {
                        showInterstitialAd {
                            navController.popBackStack()
                        }
                    } else {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    currentScreen: AppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
) {
    CenterAlignedTopAppBar(title = { Text(currentScreen.title) },
        modifier = Modifier.background(MaterialTheme.colorScheme.primary),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                }
            }
        })
}