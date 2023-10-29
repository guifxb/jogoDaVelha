package com.gfbdev.myapplication.presentation.navigation

import androidx.compose.foundation.background
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gfbdev.myapplication.presentation.screens.GameScreen
import com.gfbdev.myapplication.presentation.screens.PastGamesScreen
import com.gfbdev.myapplication.presentation.screens.StartScreen
import com.gfbdev.myapplication.presentation.viewModel.GameViewModel
import org.koin.androidx.compose.getViewModel


enum class AppScreen(val title: String) {
    START("Jogo da Velha"),
    GAME("Partida"),
    PAST_GAMES("Histórico de Partidas"),
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavController(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen =
        AppScreen.valueOf(backStackEntry?.destination?.route ?: AppScreen.START.name)

    val gameViewModel = getViewModel<GameViewModel>()

    Scaffold(modifier.fillMaxSize(),
        topBar = {
            AppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
            )
        }) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = AppScreen.START.name,
            ) {

                composable(route = AppScreen.START.name)
                {
                    StartScreen(
                        onGameClicked = {boardSize ->
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

                composable(route = AppScreen.GAME.name)
                {
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

                composable(route = AppScreen.PAST_GAMES.name)
                {
                    PastGamesScreen(
                        modifier = modifier,
                        pastPlays = gameViewModel.pastPlays.collectAsState().value,
                        onDeleteAll = { gameViewModel.deleteAll() }
                    )
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
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(title = { Text(currentScreen.title) },
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }
            }
        })
}