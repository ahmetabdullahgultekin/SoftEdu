package com.gultekinahmetabdullah.softedu.drawer


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.database.FirestoreConstants
import com.gultekinahmetabdullah.softedu.theme.getCustomOutlinedTextFieldColors

@Composable
fun FeedbackScreen() {
    var feedback by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = feedback,
            onValueChange = { feedback = it },
            label = { Text("Enter your feedback") },
            colors = getCustomOutlinedTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
            sendFeedbackToFirestore(feedback, context)
            feedback = ""
        }) {
            Text("Submit Feedback")
        }
    }
}

private fun sendFeedbackToFirestore(feedback: String, context: Context) {
    val userId = Firebase.auth.currentUser?.uid
    val email =  Firebase.auth.currentUser?.email
    val db = Firebase.firestore
    val feedbackData = hashMapOf(
        FirestoreConstants.FIELD_EMAIL to email,
        FirestoreConstants.FIELD_USER_ID to userId,
        FirestoreConstants.FIELD_FEEDBACK to feedback,
        FirestoreConstants.FIELD_TIMESTAMP to FieldValue.serverTimestamp()
    )

    db.collection(FirestoreConstants.COLLECTION_FEEDBACKS)
        .add(feedbackData)
        .addOnSuccessListener {
            Toast.makeText(context, "Feedback submitted", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error submitting feedback", Toast.LENGTH_SHORT).show()
        }
}