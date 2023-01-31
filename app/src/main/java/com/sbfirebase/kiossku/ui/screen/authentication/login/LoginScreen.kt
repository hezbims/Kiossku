package com.sbfirebase.kiossku.ui.screen.authentication.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.route.AllRoute
import com.sbfirebase.kiossku.ui.navigation.replaceAndNavigate
import com.sbfirebase.kiossku.ui.theme.GreenKiossku
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun LoginScreen(
    viewModel : LoginViewModel = hiltViewModel(),
    navController : NavHostController
){
    val uiState = viewModel.uiState.collectAsState().value

    if (uiState.loginResponse is AuthorizedApiResponse.Success) {
        viewModel.doneLoggingIn()
        navController.replaceAndNavigate(route = AllRoute.Home.route)
    }
    else
        LoginScreen(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            navigateToRegister = {
                navController.replaceAndNavigate(AllRoute.Auth.Register.route)
            }
        )
}

@Composable
private fun LoginScreen(
    uiState : LoginScreenUiState,
    onEvent : (LoginScreenEvent) -> Unit,
    navigateToRegister : () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .align(Alignment.TopStart)
        ) {
            Image(
                painter = painterResource(id = R.drawable.kiossku_header),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 56.dp)
                    .width(176.dp)
                    .height(44.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Hai, kemana aja kamu?",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(top = 64.dp)
            )
            Text(
                text = "Login untuk mengakses fitur kiossku lagi",
                fontSize = 12.sp
            )

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { onEvent(LoginScreenEvent.ChangeEmail(it)) },
                placeholder = {
                    Text("Email")
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = { onEvent(LoginScreenEvent.ChangePassword(it)) },
                placeholder = {
                    Text("Password")
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Button(
                onClick = { onEvent(LoginScreenEvent.Authenticate) },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(48.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (uiState.loginResponse is AuthorizedApiResponse.Loading)
                    CircularProgressIndicator()
                else
                    Text("Login")
            }
        }

        val gotoRegisterText = remember {
            buildAnnotatedString {
                append("Belum punya akun? ")

                pushStringAnnotation(tag = "spanned text", annotation = "")
                withStyle(
                    style = SpanStyle(
                        color = GreenKiossku,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Yuk daftar!")
                }
                pop()
            }
        }

        ClickableText(
            text = gotoRegisterText,
            onClick = { offset ->
                gotoRegisterText.getStringAnnotations(
                    tag = "spanned text",
                    start = offset,
                    end = offset
                ).firstOrNull()?.let {
                    navigateToRegister()
                }
            },
            modifier = Modifier
                .padding(bottom = 60.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
@Preview
private fun LoginScreenPreview(){
    KiosskuTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LoginScreen(
                uiState = LoginScreenUiState(),
                onEvent = {},
                navigateToRegister = {}
            )
        }
    }
}