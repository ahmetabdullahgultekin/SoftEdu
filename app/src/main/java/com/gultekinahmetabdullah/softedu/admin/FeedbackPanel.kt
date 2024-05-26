package com.gultekinahmetabdullah.softedu.admin

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.R
import com.gultekinahmetabdullah.softedu.theme.getCustomOutlinedTextFieldColors
import kotlinx.coroutines.launch

data class Feedback(val id: String, val message: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackPanel(navController: NavHostController) {

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(
            color = MaterialTheme.colorScheme.primary,
            shape = MaterialTheme.shapes.medium
        ),
        topBar = {
            TopAppBar(
                colors = TopAppBarColors(
                    MaterialTheme.colorScheme.onPrimary,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.outline,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary
                ),
                title = { Text("Feedback Panel") },
                actions = {
                    IconButton(modifier = Modifier.padding(10.dp), onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }) {

        FeedbackPanelContent(it)
    }
}

@Composable
fun FeedbackPanelContent(pd: PaddingValues) {

    var editingFeedbackId by remember { mutableStateOf<String?>(null) }
    var editingText by remember { mutableStateOf("") }
    var deletingFeedbackId by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    val db = Firebase.firestore
    var feedbacks by remember { mutableStateOf(listOf<Feedback>()) }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.primary)
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
                .padding(pd),
            colors = getCustomOutlinedTextFieldColors()
        )

        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                CircularProgressIndicator(
                    Modifier
                        .padding(16.dp)
                        .size(50.dp)
                        .align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.outline,
                    strokeWidth = 5.dp,
                )
            }
        }

        LaunchedEffect(key1 = Unit) {
            scope.launch {
                try {
                    fetchFeedbacks(db) { newFeedbacks ->
                        feedbacks = newFeedbacks
                    }
                } catch (e: Exception) {
                    Log.e("FeedbackPanel", "Error fetching feedbacks", e)
                }
            }
        }

        feedbacks.filter { it.message.contains(searchQuery, ignoreCase = true) }
            .forEach { feedback ->
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
        isLoading = false
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
                    modifier = Modifier.weight(1f),
                    colors = getCustomOutlinedTextFieldColors()
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
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Edit"
                    )
                }

                if (isDeleting) {
                    IconButton(onClick = { onConfirmDeleteClick(feedback) }) {
                        Icon(Icons.Default.Check, contentDescription = "Confirm Delete")
                    }
                } else {
                    IconButton(onClick = { onDeleteClick(feedback) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = "Delete"
                        )
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