package com.example.circuler.presentation.dummy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import com.example.circuler.presentation.core.extension.noRippleClickable
import com.example.circuler.presentation.core.extension.showToast
import com.example.circuler.presentation.core.util.UiState
import com.example.circuler.ui.theme.CirculerTheme

@Composable
fun ReqresRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: ReqresViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.getMyJogboList()
    }

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is ReqresSideEffect.ShowToast -> context.showToast(message = sideEffect.message)
                }
            }
    }

    ReqresScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
        state = state.uiState
    )
}

@Composable
fun ReqresScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    state: UiState<String>,
    modifier: Modifier = Modifier
) {
    val screenWeigth = LocalConfiguration.current.screenWidthDp
    val height = (screenWeigth * 0.5).dp

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            is UiState.Loading -> {
                item {
                    Text(
                        modifier = Modifier
                            .noRippleClickable { navigateUp() },
                        text = "로딩 중...",
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp
                    )
                }
            }

            is UiState.Failure -> {
                item {
                    Text(
                        modifier = Modifier
                            .noRippleClickable { navigateUp() },
                        text = "데이터 불러오기 실패",
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp
                    )
                }
            }

            is UiState.Success -> {
                item {
                    AsyncImage(
                        model = "https://i.pinimg.com/236x/12/95/67/1295676da767fa8171baf8a307b5786c.jpg",
                        contentDescription = null,
                        modifier = Modifier.size(height),
                        contentScale = ContentScale.Crop
                    )
                }

                item {
                    Text(
                        modifier = Modifier.padding(top = 30.dp),
                        text = state.data,
                        style = CirculerTheme.typography.heading1B20,
                        color = CirculerTheme.colors.green1,
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ReqresScreenPreview() {
    CirculerTheme {
        ReqresScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            state = UiState.Success("성공")
        )
    }
}
