package com.gultekinahmetabdullah.softedu.util

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.database.FirestoreConstants

fun fetchQuestion(userId: String?, questionCounter: Int, totalQuestions: Int, askedQuestionIds: List<String>, isTestScreen: Boolean, onSuccess: (String, String, List<String>, Int) -> Unit) {
    val db = Firebase.firestore

    userId?.let { userId ->
        db.collection(FirestoreConstants.COLLECTION_USERS).document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {  // If the user document exists
                        // Calculate the current difficulty level
                        val correctQuestions = document.get(FirestoreConstants.FIELD_CORRECT_QUESTIONS) as? List<String> ?: emptyList()

                        val difficultyLevel = if (isTestScreen) {
                            questionCounter / (totalQuestions / 5) + 1
                        } else {
                            document.getLong(FirestoreConstants.FIELD_EXPERIENCE_LEVEL)?.toInt() ?: 1
                        }
                        // Fetch each question that is not in the askedQuestionIds array
                        val query = if (isTestScreen) {
                            db.collection(FirestoreConstants.COLLECTION_QUESTIONS).whereEqualTo(FirestoreConstants.FIELD_DIFFICULTY_LEVEL, difficultyLevel)
                        } else {
                            db.collection(FirestoreConstants.COLLECTION_QUESTIONS).whereGreaterThanOrEqualTo(FirestoreConstants.FIELD_DIFFICULTY_LEVEL, difficultyLevel)
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
                                        val questionText = document.getString(FirestoreConstants.FIELD_QUESTION_TEXT) ?: ""
                                        val choices = document.get(FirestoreConstants.FIELD_CHOICES) as List<String>
                                        val correctChoice = document.getLong(FirestoreConstants.FIELD_CORRECT_CHOICE)?.toInt() ?: - 1
                                        onSuccess(questionId, questionText, choices, correctChoice)
                                    }
                                }
                    }
                }
    }
}