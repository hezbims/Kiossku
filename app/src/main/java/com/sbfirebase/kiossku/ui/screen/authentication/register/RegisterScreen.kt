package com.sbfirebase.kiossku.ui.screen.authentication.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.constant.ApiMessage
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.route.AllRoute
import com.sbfirebase.kiossku.ui.navigation.replaceAndNavigate
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.WithError
import com.sbfirebase.kiossku.ui.theme.GreenKiossku
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme
import com.sbfirebase.kiossku.ui.utils.phoneVisualTransformation

@Composable
fun RegisterScreen(
    viewModel : RegisterViewModel = hiltViewModel(),
    navController : NavHostController
){
    val uiState = viewModel.uiState.collectAsState().value
    val goToLogin : () -> Unit = { navController.replaceAndNavigate(AllRoute.Auth.Login.route) }

    when (uiState.apiResponse){
        is AuthorizedApiResponse.Success -> {
            viewModel.resetApiState()
            Toast.makeText(
                LocalContext.current,
                "Berhasil mendaftarkan akun!",
                Toast.LENGTH_SHORT
            ).show()
            goToLogin()
        }
        else -> {
            if (uiState.apiResponse?.errorMessage == ApiMessage.INTERNET_FAILED){
                viewModel.resetApiState()
                Toast.makeText(
                    LocalContext.current ,
                    "Gagal mengirim data, periksa internet anda!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            RegisterScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                goToLogin = goToLogin
            )
        }
    }

}

@Composable
private fun RegisterScreen(
    uiState: RegisterUiState,
    onEvent : (RegisterScreenEvent) -> Unit,
    goToLogin: () -> Unit
){
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.kiossku_header),
            contentDescription = null,
            modifier = Modifier
                .padding(56.dp)
                .height(44.dp)
                .width(171.dp)
        )

        Column(
            modifier = Modifier
                .padding(top = 64.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            OutlinedTextField(
                value = uiState.fullName,
                onValueChange = { onEvent(RegisterScreenEvent.OnChangeFullName(it)) },
                modifier = Modifier
                    .fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null
                    )
                },
                placeholder = { Text("Nama lengkap") },
            )

            WithError(
                errorMessage = uiState.emailError,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { onEvent(RegisterScreenEvent.OnChangeEmail(it)) },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Email ,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = { Text("Email") }
                )
            }


            WithError(
                errorMessage = uiState.teleponError,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = uiState.telepon,
                    onValueChange = { onEvent(RegisterScreenEvent.OnChangeTelepon(it)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    // placeholder = { Text("Nomor telepon") },
                    visualTransformation = { phoneVisualTransformation(it) },
                    label = { Text("Nomor telepon ") }
                )
            }


            WithError(
                errorMessage = uiState.passwordError,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { onEvent(RegisterScreenEvent.OnChangePassword(it)) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Visibility,
                            contentDescription = null
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = null
                        )
                    },
                    placeholder = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }


            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = { onEvent(RegisterScreenEvent.OnChangeConfirmPassword(it)) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Visibility,
                        contentDescription = null
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null
                    )
                },
                placeholder = { Text("Konfirmasi password") },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }


        Button(
            onClick = { onEvent(RegisterScreenEvent.OnSubmitData) },
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth(),
            enabled = uiState.apiResponse !is AuthorizedApiResponse.Loading,
            contentPadding = PaddingValues(0.dp)
        ) {
            if (uiState.apiResponse is AuthorizedApiResponse.Loading)
                CircularProgressIndicator()
            else
                Text("Daftar")
        }

        AnnotatedGoToLoginText(
            goToLogin = goToLogin,
            modifier = Modifier.padding(
                top = 81.dp
            )
        )
    }
}

@Composable
fun AnnotatedGoToLoginText(
    goToLogin: () -> Unit,
    modifier: Modifier = Modifier
){
    val annotatedText = remember {
        buildAnnotatedString {
            append("Sudah punya akun? ")

            pushStringAnnotation("goToLogin", "")

            withStyle(
                style = SpanStyle(
                    color = GreenKiossku,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("Yuk masuk")
            }
            pop()
        }
    }

    ClickableText(
        text = annotatedText ,
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = "goToLogin" ,
                start = offset ,
                end = offset
            ).firstOrNull()?.let{
                goToLogin()
            }
        },
        modifier = modifier
    )
}

@Composable
@Preview
private fun RegisterScreenPreview(){
    KiosskuTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            RegisterScreen(
                uiState = RegisterUiState(),
                onEvent = {},
                goToLogin = {}
            )
        }
    }
}

