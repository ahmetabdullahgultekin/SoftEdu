package com.gultekinahmetabdullah.softedu.signinsignup

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Composable
fun LoginScreen(navController: NavController, currentUser: FirebaseUser) {//TODO Signup Level add determination test
    val context = LocalContext.current
    val auth: FirebaseAuth = Firebase.auth

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Sign in successful!", Toast.LENGTH_SHORT).show()
                            //navController.navigate(Screen.MainScreen.Main.mRoute)
                            //Crash yiyor saçma statement
                            //TODO Problem here
                            // Navigate to "main" screen
                            //If current user value changes, screen will change


                        } else {
                            Toast.makeText(context,
                                "Please enter valid address.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(
                    context, "Please enter email and password.", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Sign In")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Sign up successful!", Toast.LENGTH_SHORT).show()
                            // Navigate to "main" screen
                            //navController.navigate(Screen.MainScreen.Main.mRoute)
                        } else {
                            Toast.makeText(
                                context, "Sign up incomplete.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Please enter email and password.", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Sign Up")
        }
    }
}
