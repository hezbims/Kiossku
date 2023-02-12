package com.sbfirebase.kiossku.ui.screen.profile.update_profile_dialog

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.model.UserData
import com.sbfirebase.kiossku.ui.MainActivity
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.WithError
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme
import com.sbfirebase.kiossku.ui.utils.phoneVisualTransformation
import dagger.hilt.android.EntryPointAccessors


@Composable
fun UpdateProfileDialog(
    initialData : UserData,
    doneUpdatingProfile : (UserData) -> Unit,
    navController : NavHostController
){
    val viewModel = getProfileDialogViewModel(initialData = initialData)
    val uiState = viewModel.uiState.collectAsState().value

    if (uiState.updateUserResponse is AuthorizedApiResponse.Success) {
        viewModel
            .onEvent(ProfileDialogEvent.DoneSubmitting)
        doneUpdatingProfile(
            UserData(
                namaLengkap = uiState.namaLengkap,
                email = uiState.email,
                nomorTelepon = "62${uiState.nomorTelepon}"
            )
        )
        navController.popBackStack()
    }
    else
        UpdateProfileDialog(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            onDismissDialog = { navController.popBackStack() }
        )
}

@Composable
private fun UpdateProfileDialog(
    uiState: ProfileDialogUiState,
    onEvent : (ProfileDialogEvent) -> Unit,
    onDismissDialog : () -> Unit,
){
    KiosskuTheme {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Card(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.TopEnd)
                            .clickable { onDismissDialog() },
                        shape = CircleShape,
                        border = BorderStroke(width = 0.5.dp, Color.Black)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(12.dp)
                        )
                    }

                    Text(
                        text = "Edit Detail Profil",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    Text(
                        text = "Isi data anda dengan benar!",
                        color = Color(0xFF54514F),
                        fontSize = 12.sp
                    )


                    OutlinedTextField(
                        value = uiState.namaLengkap,
                        onValueChange = { onEvent(ProfileDialogEvent.ChangeNamaLengkap(it)) },
                        label = {
                            Text(
                                text = "Nama lengkap"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    WithError(
                        errorMessage = uiState.emailError,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = uiState.email,
                            onValueChange = { onEvent(ProfileDialogEvent.ChangeEmail(it)) },
                            label = {
                                Text(
                                    text = "Email"
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }


                    WithError(
                        errorMessage = uiState.nomorTeleponError,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = uiState.nomorTelepon,
                            onValueChange = { onEvent(ProfileDialogEvent.ChangeNomorTelepon(it)) },
                            label = {
                                Text(
                                    text = "Nomor telepon"
                                )
                            },
                            visualTransformation = { phoneVisualTransformation(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }


                    Button(
                        onClick = { onEvent(ProfileDialogEvent.Submit) },
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth(),
                        enabled = uiState.updateUserResponse !is AuthorizedApiResponse.Loading
                    ) {
                        if (uiState.updateUserResponse is AuthorizedApiResponse.Loading)
                            CircularProgressIndicator()
                        else
                            Text("Kirim")
                    }
                }
            }
        }
    }
}


@Composable
private fun getProfileDialogViewModel(
    initialData : UserData
) : ProfileDialogViewModel {
    val assistedFactory = EntryPointAccessors.fromActivity(
        unwrapContext(LocalContext.current),
        MainActivity.ProfileDialogViewModelFactoryProvider::class.java
    ).getFactory()

    return viewModel(
        factory = ProfileDialogViewModel.getFactory(
            assistedFactory = assistedFactory,
            initialData = initialData
        )
    )
}

private fun unwrapContext(context  : Context) : Activity{
    var result = context
    while (result !is Activity && result is ContextWrapper)
        result = result.baseContext

    return result as Activity
}

@Composable
@Preview
private fun UpdateProfileDialogPreview(){
    KiosskuTheme {
        Surface{
            UpdateProfileDialog(
                uiState = ProfileDialogUiState(
                    email = "hezbisulaiman@gmail.com",
                    nomorTelepon = "87876884620",
                    namaLengkap = "Hezbi Sulaiman"
                ),
                onEvent = {},
                onDismissDialog = {}
            )
        }
    }
}