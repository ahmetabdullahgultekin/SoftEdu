package com.gultekinahmetabdullah.softedu.util

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun fetchQuestion(userId: String?, questionCounter: Int, totalQuestions: Int, askedQuestionIds: List<String>, isTestScreen: Boolean, onSuccess: (String, String, List<String>, Int) -> Unit) {
    val db = Firebase.firestore

    userId?.let { userId ->
        db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {  // If the user document exists
                        // Calculate the current difficulty level
                        val correctQuestions = document.get("correctQuestions") as? List<String> ?: emptyList()

                        val difficultyLevel = if (isTestScreen) {
                            questionCounter / (totalQuestions / 5) + 1
                        } else {
                            document.getLong("experienceLevel")?.toInt() ?: 1
                        }
                        // Fetch each question that is not in the askedQuestionIds array
                        val query = if (isTestScreen) {
                            db.collection("questions").whereEqualTo("difficultyLevel", difficultyLevel)
                        } else {
                            db.collection("questions").whereGreaterThanOrEqualTo("difficultyLevel", difficultyLevel)
                        }

                        // Fetch each question that is not in the askedQuestionIds array
                        query.get()
                                .addOnSuccessListener { result ->
                                    val documents = result.filter { document ->
                                        (! askedQuestionIds.contains(document.id) && ! correctQuestions.contains(document.id))
                                    }
                                    if (documents.isNotEmpty()) {
                                        val document = documents.random()
                                        val questionId = document.id
                                        val questionText = document.getString("questionText") ?: ""
                                        val choices = document.get("choices") as List<String>
                                        val correctChoice = document.getLong("correctChoice")?.toInt() ?: - 1
                                        onSuccess(questionId, questionText, choices, correctChoice)
                                    }
                                }
                    }
                }
    }
}