package com.gultekinahmetabdullah.softedu.signinsignup

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.gultekinahmetabdullah.softedu.R
import com.gultekinahmetabdullah.softedu.util.Screen
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("RestrictedApi")
@Composable
fun LoginScreen(auth: FirebaseAuth, navController: NavHostController) {
    val context = LocalContext.current
    //val auth: FirebaseAuth = Firebase.auth

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var numberOfLight by remember { mutableIntStateOf(6) }
    var isLight by remember { mutableStateOf(false) }
    val color = animateColorAsState(
        if (isLight) MaterialTheme.colorScheme.onPrimary
        else Color.Transparent,
        label = "storming"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        LaunchedEffect(key1 = true) {
            while (true) {
                if (numberOfLight > 0) {
                    isLight = ! isLight
                    -- numberOfLight;
                    delay(100)
                } else {
                    numberOfLight = 6
                    delay(2000)
                }
            }
        }

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Storm",
            tint = color.value,
            modifier = Modifier
                .size(300.dp)
                .padding(16.dp)
                .background(color = MaterialTheme.colorScheme.primary)
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedLabelColor = MaterialTheme.colorScheme.outline,
                unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                focusedContainerColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,

                cursorColor = MaterialTheme.colorScheme.onPrimary,
                selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.onPrimary,
                    backgroundColor = MaterialTheme.colorScheme.onPrimary
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        //Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedLabelColor = MaterialTheme.colorScheme.outline,
                unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                focusedContainerColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,

                cursorColor = MaterialTheme.colorScheme.onPrimary,
                selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.onPrimary,
                    backgroundColor = MaterialTheme.colorScheme.onPrimary
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Button(colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        ),
               onClick = {
                   if (email.isNotEmpty() && password.isNotEmpty()) {
                       auth.signInWithEmailAndPassword(email, password)
                           .addOnCompleteListener { task ->
                               if (task.isSuccessful) {
                                   Toast.makeText(context, "Sign in successful!", Toast.LENGTH_SHORT)
                                       .show()
                                   while (navController.currentBackStack.value.isNotEmpty()) {
                                       navController.popBackStack()
                                   }
                                   navController.navigate(Screen.BottomScreen.Home.bRoute)
                               } else {
                                   Toast.makeText(
                                       context,
                                       task.exception?.message, Toast.LENGTH_LONG
                                   ).show()
                               }
                           }
                   } else {
                       Toast.makeText(
                           context, "Please enter email and password.", Toast.LENGTH_SHORT
                       ).show()
                   }
               }) {
            Text("Sign In")
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = "You have no account?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp)
            )
            ClickableText(text = AnnotatedString("Sign Up"),
                          onHover = {},
                          style = TextStyle.Default.copy(
                              color = MaterialTheme.colorScheme.onPrimary, // Change the color here
                              textDecoration = TextDecoration.Underline,
                              fontWeight = FontWeight.Bold,
                              fontSize = TextUnit(16f, TextUnitType.Sp)
                          ),

                          onClick = {
                              if (email.isNotEmpty() && password.isNotEmpty()) {
                                  auth.createUserWithEmailAndPassword(email, password)
                                      .addOnCompleteListener { task ->
                                          if (task.isSuccessful) {
                                              Toast.makeText(
                                                  context,
                                                  "Sign up successful!",
                                                  Toast.LENGTH_SHORT
                                              )
                                                  .show()
                                              // Navigate to "main" screen
                                              navController.navigate(Screen.LoginScreen.UserInfo.route)
                                          } else {
                                              Toast.makeText(
                                                  context,
                                                  "Sign up failed. Please try to sign up" +
                                                      " with valid email address again.",
                                                  Toast.LENGTH_SHORT
                                              ).show()
                                          }
                                      }
                              } else {
                                  Toast.makeText(
                                      context,
                                      "Please enter email and password.",
                                      Toast.LENGTH_SHORT
                                  )
                                      .show()
                              }
                          }
            )
        }

        //Spacer(modifier = Modifier.height(8.dp))

        /*
        Button(colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        ),
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Sign up successful!", Toast.LENGTH_SHORT)
                                    .show()
                                // Navigate to "main" screen
                                navController.navigate(Screen.LoginScreen.UserInfo.route)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Sign up failed. Please try to sign up" +
                                            " with valid email address again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Please enter email and password.", Toast.LENGTH_SHORT)
                        .show()
                }
            }) {
            Text("Sign Up")
        }

         */
    }
}
