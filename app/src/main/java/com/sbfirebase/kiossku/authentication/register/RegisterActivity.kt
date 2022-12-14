package com.sbfirebase.kiossku.authentication.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.databinding.ActivityRegisterBinding
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sbfirebase.kiossku.authentication.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityRegisterBinding.inflate(layoutInflater)

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(this@RegisterActivity))
            setContent {
                KiosskuTheme {
                    RegisterScreen(
                        goToLogin = {
                            startActivity(
                                Intent(
                                    this@RegisterActivity,
                                    LoginActivity::class.java
                                )
                            )
                            this@RegisterActivity.finish()
                        },
                        viewModel = viewModel(
                            factory = RegisterViewModelFactory(application)
                        )
                    )
                }
            }
        }

        setContentView(binding.root)


    }
}

@Composable
fun RegisterScreen(
    goToLogin : () -> Unit,
    viewModel : RegisterViewModel? = null,
    modifier: Modifier = Modifier
){
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier.padding(start = 24.dp , end = 24.dp , top = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var email by rememberSaveable(stateSaver = TextFieldValue.Saver){
                mutableStateOf(TextFieldValue(""))
            }
            var password by rememberSaveable(stateSaver = TextFieldValue.Saver){
                mutableStateOf(TextFieldValue(""))
            }
            var phone by rememberSaveable(stateSaver = TextFieldValue.Saver){
                mutableStateOf(TextFieldValue(""))
            }
            var fullName by rememberSaveable(stateSaver = TextFieldValue.Saver){
                mutableStateOf(TextFieldValue(""))
            }
            var confirmPassword by rememberSaveable(stateSaver = TextFieldValue.Saver){
                mutableStateOf(TextFieldValue(""))
            }

            Image(
                painter = painterResource(id = R.drawable.kiossku_header),
                contentDescription = null,
                modifier = Modifier
                    .height(44.dp)
                    .width(171.dp)
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                modifier = Modifier
                    .padding(top = 64.dp)
                    .fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null
                    )
                },
                placeholder = { Text("Nama lengkap") },
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Email ,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                placeholder = { Text("Email") }
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                placeholder = { Text("Nomor telepon") },
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
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
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
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
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel!!.register(
                        fullName = fullName.text,
                        email = email.text,
                        telepon = phone.text,
                        password = password.text,
                        confirmPassword = password.text
                    )
                },
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                enabled = !(viewModel!!.sedangMendaftarkanAkun.collectAsState().value)
            ) {
                Text("Daftar")
            }

            AnnotatedGoToLoginText(
                goToLogin = goToLogin,
                modifier = Modifier.padding(
                    top = 81.dp ,
                    bottom = 60.dp
                )
            )
        }
    }
}

@Composable
fun AnnotatedGoToLoginText(
    goToLogin: () -> Unit,
    modifier: Modifier = Modifier
){
    val annotatedText = buildAnnotatedString {
        // append string biasa
        append("Sudah punya akun? ")

        // memberikan annotation / tanda untuk setiap string
        // yang masuk setelah ini
        pushStringAnnotation("goToLogin" , "")

        withStyle(
            style = SpanStyle(
                color = Color.Blue,
                fontWeight = FontWeight.Bold
            )
        ){
            append("Yuk masuk")
        }

        // membuang annotation / style yang paling atas
        pop()
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

@Preview
@Composable
fun RegisterScreenPreview(){
    MaterialTheme {
        RegisterScreen(
            goToLogin = {}
        )
    }
}