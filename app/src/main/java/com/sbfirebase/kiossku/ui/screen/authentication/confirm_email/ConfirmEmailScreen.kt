package com.sbfirebase.kiossku.ui.screen.authentication.confirm_email

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun ConfirmEmailScreen(
    viewModel: ConfirmEmailViewModel = hiltViewModel(),
    navController : NavHostController
){
    val uiState = viewModel.uiState.collectAsState().value
    when (uiState.submitTokenResponse){
        is ApiResponse.Failure -> {

        }
        is ApiResponse.Success -> {

        }
        else -> {

        }
    }
}

@Composable
private fun ConfirmEmailScreen(
    apiResponse: ApiResponse<String>,
    email : String,
    uiState : ConfirmEmailScreenUiState,
    onEvent : (ConfirmEmailScreenEvent) -> Unit
){
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.confirm_email_image),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 42.dp)
                    .size(233.dp)
            )

            Text(
                text = "Verifikasi Kode",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 48.dp)
            )

            val topPadding = Modifier.padding(top = 24.dp)
            when (apiResponse) {
                is ApiResponse.Loading -> {
                    CircularProgressIndicator(modifier = topPadding)
                    Text(
                        text = emailAnnotatedString(
                            message = "sedang mengirim kode verifikasi ke email : ",
                            email = email
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(250.dp)
                    )
                }
                is ApiResponse.Success -> {
                    Text(
                        text = emailAnnotatedString(
                            message = "kode verifikasi telah dikirim di email : ",
                            email = email
                        ),
                        textAlign = TextAlign.Center,
                        modifier = topPadding.width(250.dp)
                    )
                }
                else -> {
                    Text(
                        text = emailAnnotatedString(
                            message = "gagal mengirim kode verifikasi ke email : ",
                            email = email,
                            additionalMessage = ". Tekan untuk mengirim ulang kode verifikasi"
                        ),
                        color = MaterialTheme.colors.error,
                        textAlign = TextAlign.Center,
                        modifier = topPadding
                            .width(250.dp)
                            .clickable { onEvent(ConfirmEmailScreenEvent.OnSendToken) }
                    )
                }
            }

            OutlinedTextField(
                value = uiState.verificationCode,
                onValueChange = { onEvent(ConfirmEmailScreenEvent.OnChangeVerificationCode(it)) },
                placeholder = {
                    Text(
                        text = "Masukkan kode verifikasi",
                        style = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(top = 32.dp, start = 18.dp, end = 18.dp)
                    .fillMaxWidth()
            )
        }
        Button(
            onClick = { onEvent(ConfirmEmailScreenEvent.OnSubmitToken) },
            enabled = uiState.submitTokenResponse !is ApiResponse.Loading,
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .padding(bottom = 106.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth()
                .height(48.dp)
                .align(Alignment.BottomCenter)
        ) {
            if (uiState.submitTokenResponse is ApiResponse.Loading)
                CircularProgressIndicator()
            else
                Text("Kirim")
        }
    }
}

@Composable
private fun emailAnnotatedString(
    message : String ,
    email : String,
    additionalMessage : String? = null
) : AnnotatedString =
    remember {
        buildAnnotatedString {
            append(message)

            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(email)
            }

            additionalMessage?.let {
                append(it)
            }
        }
    }

@Composable
@Preview
private fun ConfirmEmailScreenLoadingPreview(){
    KiosskuTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            ConfirmEmailScreen(
                apiResponse = ApiResponse.Loading(),
                email = "hezbisulaiman@gmail.com",
                uiState = ConfirmEmailScreenUiState(),
                onEvent = {}
            )
        }
    }
}

@Composable
@Preview
private fun ConfirmEmailScreenFailedPreview(){
    KiosskuTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            ConfirmEmailScreen(
                apiResponse = ApiResponse.Failure(),
                email = "hezbisulaiman@gmail.com",
                uiState = ConfirmEmailScreenUiState().copy(
                    sendTokenToEmailResponse = ApiResponse.Failure()
                ),
                onEvent = {}
            )
        }
    }
}