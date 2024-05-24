package com.gultekinahmetabdullah.softedu.admin

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.R

data class Feedback(val id: String, val message: String)

@Composable
fun FeedbackPanel() {
    val db = Firebase.firestore
    var feedbacks by remember { mutableStateOf(listOf<Feedback>()) }
    var editingFeedbackId by remember { mutableStateOf<String?>(null) }
    var editingText by remember { mutableStateOf("") }
    var deletingFeedbackId by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        fetchFeedbacks(db) { newFeedbacks ->
            feedbacks = newFeedbacks
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .clickable {
                deletingFeedbackId = null
                editingFeedbackId = null
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Feedbacks") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        feedbacks.filter { it.message.contains(searchQuery, ignoreCase = true) }.forEach { feedback ->
            FeedbackItem(
                feedback = feedback,
                onEditClick = { editingFeedbackId = it.id; editingText = it.message },
                onDeleteClick = { deletingFeedbackId = it.id },
                onConfirmDeleteClick = { feedback ->
                    deleteFeedback(db, feedback.id)
                    feedbacks = feedbacks.filter { it.id != feedback.id }
                    deletingFeedbackId = null
                },
                onConfirmEditClick = { feedback, newText ->
                    updateFeedback(db, feedback.id, newText)
                    feedbacks = feedbacks.map {
                        if (it.id == feedback.id) Feedback(it.id, newText)
                        else it
                    }
                    editingFeedbackId = null
                },
                isEditing = editingFeedbackId == feedback.id,
                editingText = editingText,
                onEditingTextChange = { editingText = it },
                isDeleting = deletingFeedbackId == feedback.id,
            )
        }
    }
}

@Composable
fun FeedbackItem(
    feedback: Feedback,
    onEditClick: (Feedback) -> Unit,
    onDeleteClick: (Feedback) -> Unit,
    onConfirmDeleteClick: (Feedback) -> Unit,
    onConfirmEditClick: (Feedback, String) -> Unit,
    isEditing: Boolean,
    editingText: String,
    onEditingTextChange: (String) -> Unit,
    isDeleting: Boolean
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isEditing) {
                OutlinedTextField(
                    value = editingText,
                    onValueChange = onEditingTextChange,
                    label = { Text("Edit Feedback") },
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        onConfirmEditClick(feedback, editingText)
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Confirm")
                }
            } else {
                Text(
                    text = feedback.message,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { onEditClick(feedback) }) {
                    Icon(painter = painterResource(id = R.drawable.ic_edit), contentDescription = "Edit")
                }

                if (isDeleting) {
                    IconButton(onClick = { onConfirmDeleteClick(feedback) }) {
                        Icon(Icons.Default.Check, contentDescription = "Confirm Delete")
                    }
                } else {
                    IconButton(onClick = { onDeleteClick(feedback) }) {
                        Icon(painter = painterResource(R.drawable.ic_delete), contentDescription = "Delete")
                    }
                }
            }
        }
    }
}

fun fetchFeedbacks(db: FirebaseFirestore, onResult: (List<Feedback>) -> Unit) {
    db.collection("feedbacks")
        .get()
        .addOnSuccessListener { result ->
            val feedbacks = result.map { document ->
                Feedback(document.id, document.getString("feedback") ?: "")
            }
            onResult(feedbacks)
        }
}

fun updateFeedback(db: FirebaseFirestore, feedbackId: String, newMessage: String) {
    db.collection("feedbacks").document(feedbackId)
        .update("feedback", newMessage)
        .addOnSuccessListener {
            Log.d("AdminPanel", "Feedback successfully updated!")
        }
        .addOnFailureListener { e ->
            Log.w("AdminPanel", "Error updating feedback", e)
        }
}

fun deleteFeedback(db: FirebaseFirestore, feedbackId: String) {
    db.collection("feedbacks").document(feedbackId)
        .delete()
        .addOnSuccessListener {
            Log.d("AdminPanel", "Feedback successfully deleted!")
        }
        .addOnFailureListener { e ->
            Log.w("AdminPanel", "Error deleting feedback", e)
        }
}