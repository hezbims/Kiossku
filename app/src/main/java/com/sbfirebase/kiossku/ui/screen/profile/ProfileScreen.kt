package com.sbfirebase.kiossku.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.ui.theme.GreenKiossku
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun ProfileScreen(
    uiState : ProfileUIState,
    logout : () -> Unit,
    modifier : Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 24.dp)
    ){
       Text(
           text = "Profile anda",
           fontWeight = FontWeight.Bold,
           modifier = Modifier
               .padding(top = 59.dp)
       )

        Box{
            Image(
                painter = painterResource(id = R.drawable.kioss_home_placeholder),
                contentDescription = "Foto profil",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        end = 11.dp,
                        bottom = 11.dp,
                        start = 11.dp
                    )
                    .size(96.dp)
                    .clip(RoundedCornerShape(size = 16.dp))
            )

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.BottomEnd)
                    .defaultMinSize(1.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = GreenKiossku
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp),
                    tint = Color.White
                )
            }
        }

        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                backgroundColor = GreenKiossku
            ),
            modifier = Modifier
                .padding(top = 24.dp)
                .width(208.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Edit Profil")
        }

        Card(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
        ){
            Column(
                modifier = Modifier
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text(
                    text = "Info profil",
                    fontWeight = FontWeight.Bold
                )
                RowProfile(
                    label = "Nama",
                    data = "Fulan",
                )
                RowProfile(
                    label = "Email" ,
                    data = "fulan@gmail.com"
                )
                RowProfile(
                    label = "Nomor telepon",
                    data = "081234567890"
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.Bottom
        ){
            Button(
                onClick = {
                    logout()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = GreenKiossku,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(
                        bottom = 36.dp
                    )
                    .height(48.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                enabled = !uiState.sedangLogout
            ) {
                if (uiState.sedangLogout)
                    CircularProgressIndicator()
                else
                    Text(
                        "Log out",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
            }
        }
    }
}

@Composable
fun RowProfile(
    label : String,
    data : String,
    modifier : Modifier = Modifier
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ){
        Text(label)
        Text(data)
    }
}

@Composable
@Preview
fun ProfileScreenPreview(){
    KiosskuTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ProfileScreen(
                uiState = ProfileUIState(),
                logout = {}
            )
        }
    }
}