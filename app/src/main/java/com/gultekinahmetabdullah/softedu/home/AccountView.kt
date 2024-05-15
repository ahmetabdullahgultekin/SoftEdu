package com.gultekinahmetabdullah.softedu.home.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gultekinahmetabdullah.softedu.theme.SoftEduTheme

@Composable
fun AccountView(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row(){
                Icon(imageVector= Icons.Filled.AccountCircle,
                    contentDescription = "Account",
                    modifier = Modifier.padding(end = 8.dp)
                    )
                Column {
                    Text("agaoglum@gmail.com")
                }
            }
            IconButton(onClick = {
                //TODO : Logout

            }){
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null)
            }
        }

        Row(modifier = Modifier.padding(top = 16.dp)) {
            Icon(
                painter = painterResource(
                    id = com.gultekinahmetabdullah.softedu.R.drawable.ic_temporary
                ),
                contentDescription = "User Information Here",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Melik Ali")
        }
        HorizontalDivider()

        Row(modifier = Modifier.padding(top = 16.dp)) {
            Icon(
                painter = painterResource(
                    id = com.gultekinahmetabdullah.softedu.R.drawable.ic_temporary
                ),
                contentDescription = "User Information Here",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Deligöz")
        }
        HorizontalDivider()

        Row(modifier = Modifier.padding(top = 16.dp)) {
            Icon(
                painter = painterResource(
                    id = com.gultekinahmetabdullah.softedu.R.drawable.ic_temporary
                ),
                contentDescription = "User Information Here",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "+90-532-123-45-67")
        }
        HorizontalDivider()
    }
}
@Preview(name = "Welcome Account", showBackground = true)
@Composable
fun AccountViewScreenPreview() {
    SoftEduTheme {
        AccountView()
    }
}