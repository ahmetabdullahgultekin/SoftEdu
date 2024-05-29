package com.gultekinahmetabdullah.softedu.drawer

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gultekinahmetabdullah.softedu.theme.getCustomButtonColors

@Composable
fun Subscription(){

    val context = LocalContext.current

    Column(
        modifier = Modifier.height(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Manage Subscription")
        Card(modifier = Modifier.padding(8.dp), elevation = CardDefaults.cardElevation()){
            Column(modifier = Modifier.padding(8.dp)){
                Column {
                    Text(text = "Education")
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Free Plan")
                        TextButton(colors = getCustomButtonColors(),
                            onClick = {
                                Toast.makeText(
                                    context, "There is not an available plan now!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "See All Plans")
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "See All Plans"
                                )
                            }
                        }

                    }
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp), thickness = 1.dp)
                Row(Modifier.padding(vertical = 16.dp)) {
                    Icon(imageVector = Icons.Default.AccountBox, contentDescription = "Get a Plan")
                    Text(text = "Get a Plan")
                }
            }

        }
    }
}