package com.itpens.kiossku.ui.screen.authentication.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itpens.kiossku.route.AllRoute
import com.itpens.kiossku.ui.navigation.replaceAndNavigate
import com.itpens.kiossku.ui.screen.authentication.PasswordTextField
import com.itpens.kiossku.ui.screen.submitkios.uicomponent.WithError
import com.itpens.kiossku.ui.theme.GreenKiossku
import com.itpens.kiossku.ui.theme.KiosskuTheme
import com.itpens.kiossku.ui.utils.asString
import com.itpens.kiossku.ui.utils.phoneVisualTransformation
import com.itpens.kiossku.R

@Composable
fun RegisterScreen(
    viewModel : RegisterViewModel = hiltViewModel(),
    navController : NavHostController
){
    val uiState = viewModel.uiState.collectAsState().value
    val goToLogin : () -> Unit = {
        navController.replaceAndNavigate(AllRoute.Auth.Login.route)
    }

    LaunchedEffect(uiState.isRegistrationSuccessful) {
        if (uiState.isRegistrationSuccessful) {
            // TODO(next iteration): navigate to ConfirmEmail with the registered email
            goToLogin()
            viewModel.onEvent(RegisterScreenEvent.OnRegistrationSuccessHandled)
        }
    }

    RegisterScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goToLogin = goToLogin
    )
}

@Composable
private fun RegisterScreen(
    uiState: RegisterUiState,
    onEvent : (RegisterScreenEvent) -> Unit,
    goToLogin: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
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
            ) {
                WithError(
                    errorMessage = uiState.fullNameError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(RegisterTestTags.FULL_NAME_FIELD_CONTAINER)
                ) {
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
                        placeholder = {
                            Text(stringResource(R.string.register_full_name_placeholder))
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                }

                WithError(
                    errorMessage = uiState.emailError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(RegisterTestTags.EMAIL_FIELD_CONTAINER)
                ) {
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = { onEvent(RegisterScreenEvent.OnChangeEmail(it)) },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Email,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        placeholder = {
                            Text(stringResource(R.string.register_email_placeholder))
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                }


                WithError(
                    errorMessage = uiState.teleponError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(RegisterTestTags.TELEPON_FIELD_CONTAINER)
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
                        visualTransformation = { phoneVisualTransformation(it) },
                        label = {
                            Text(stringResource(R.string.register_phone_label))
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                }


                WithError(
                    errorMessage = uiState.passwordError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(RegisterTestTags.PASSWORD_FIELD_CONTAINER)
                ) {
                    PasswordTextField(
                        value = uiState.password,
                        onValueChange = { onEvent(RegisterScreenEvent.OnChangePassword(it)) },
                        placeholder = {
                            Text(stringResource(R.string.register_password_placeholder))
                        },
                        showPassword = uiState.showPassword,
                        onChangeVisibility = {
                            onEvent(RegisterScreenEvent.OnChangePasswordVisibility)
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                }


                WithError(
                    errorMessage = uiState.confirmPasswordError?.asString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(RegisterTestTags.CONFIRM_PASSWORD_FIELD_CONTAINER)
                ) {
                    PasswordTextField(
                        value = uiState.confirmPassword,
                        onValueChange = { onEvent(RegisterScreenEvent.OnChangeConfirmPassword(it)) },
                        placeholder = {
                            Text(
                                stringResource(
                                    R.string.register_confirm_password_placeholder
                                )
                            )
                        },
                        showPassword = uiState.showConfirmPassword,
                        onChangeVisibility = {
                            onEvent(RegisterScreenEvent.OnChangeConfirmPasswordVisibility)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            uiState.generalError?.let { generalError ->
                Text(
                    text = generalError.asString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    color = MaterialTheme.colors.error
                )
            }

            Button(
                onClick = { onEvent(RegisterScreenEvent.OnSubmitData) },
                modifier = Modifier
                    .padding(top = 24.dp)
                    .height(48.dp)
                    .fillMaxWidth(),
                enabled = !uiState.isSubmitting,
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (uiState.isSubmitting)
                    CircularProgressIndicator()
                else
                    Text(stringResource(R.string.register_submit_button))
            }

            AnnotatedGoToLoginText(
                goToLogin = goToLogin,
                modifier = Modifier.padding(
                    top = 24.dp, bottom = 48.dp
                )
            )
        }

        Card(
            elevation = 12.dp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
        ) {
            Row {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.register_back_to_login),
                    modifier = Modifier
                        .clickable { goToLogin() }
                        .padding(
                            top = 12.dp ,
                            bottom = 12.dp,
                            start = 24.dp)
                )
            }
        }
    }
}

@Composable
fun AnnotatedGoToLoginText(
    goToLogin: () -> Unit,
    modifier: Modifier = Modifier
){
    val haveAccountPrefix = stringResource(R.string.register_have_account_prefix)
    val goToLoginText = stringResource(R.string.register_go_to_login)

    val annotatedText = remember(haveAccountPrefix, goToLoginText) {
        buildAnnotatedString {
            append(haveAccountPrefix)

            pushStringAnnotation("goToLogin", "")

            withStyle(
                style = SpanStyle(
                    color = GreenKiossku,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(goToLoginText)
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
