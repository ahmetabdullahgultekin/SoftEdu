package com.gultekinahmetabdullah.softedu.database

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


//addQuestionToFirestore(2,"questionText", listOf("choice1", "choice2", "choice3","choice4"), 1)
fun addQuestionToFirestore(difficultyLevel: Int,
                           questionText: String,
                           choices: List<String>,
                           correctChoice: Int): Boolean {
    val db = Firebase.firestore
    // Create a new question with a difficulty level, question text, choices, and correct choice

    if ((correctChoice in 0 .. 3) && (difficultyLevel in 1 .. 5) && (questionText.isNotEmpty()) && (choices.isNotEmpty())) {
        val question = hashMapOf(
            FirestoreConstants.FIELD_DIFFICULTY_LEVEL to difficultyLevel,
            FirestoreConstants.FIELD_QUESTION_TEXT to questionText,
            FirestoreConstants.FIELD_CHOICES to choices,
            FirestoreConstants.FIELD_CORRECT_CHOICE to correctChoice
        )
        // Add a new document with a generated ID to the "questions" collection
        db.collection(FirestoreConstants.COLLECTION_QUESTIONS)
            .add(question)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                return@addOnSuccessListener
            }
    }
    return false
}


fun updateAnsweredQuestions(userId: String, questionId: String, isCorrect: Boolean) {
    val db = Firebase.firestore

    if (isCorrect) {
        // If the answer is correct, add the question ID to the correctQuestions array
        db.collection(FirestoreConstants.COLLECTION_USERS)
            .document(userId)
            .update(FirestoreConstants.FIELD_CORRECT_QUESTIONS, FieldValue.arrayUnion(questionId),
                    FirestoreConstants.FIELD_WRONG_QUESTIONS, FieldValue.arrayRemove(questionId),
                    FirestoreConstants.FIELD_SCORE, FieldValue.increment(FirestoreConstants.INCREASE_SCORE))


    } else {
        // If the answer is incorrect, add the question ID to the wrongQuestions array
        db.collection(FirestoreConstants.COLLECTION_USERS)
            .document(userId)
            .update(FirestoreConstants.FIELD_WRONG_QUESTIONS, FieldValue.arrayUnion(questionId))
    }
}


//        createUser("test1@gmail.com", "password","name1","surname1",1)
fun createUser(email: String, password: String, name: String, surname: String, experienceLevel: Int) {
    val auth: FirebaseAuth = Firebase.auth
    val db = Firebase.firestore

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign up success, update UI with the signed-in user's information
                val user = auth.currentUser
                Log.d(TAG, "createUserWithEmail:success. User ID is ${user?.uid}")

                // Create a new user document in Firestore
                val userProfile = hashMapOf(
                    FirestoreConstants.FIELD_NAME to name,
                    FirestoreConstants.FIELD_SURNAME to surname,
                    FirestoreConstants.FIELD_EXPERIENCE_LEVEL to experienceLevel,
                    FirestoreConstants.FIELD_SCORE to 0
                )

                user?.uid?.let { uid ->
                    db.collection(FirestoreConstants.COLLECTION_USERS)
                        .document(uid)
                        .set(userProfile)
                        .addOnSuccessListener {
                            Log.d(TAG, "User profile created in Firestore.")
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Error creating user profile in Firestore.", e)
                            e.printStackTrace()
                        }
                }
            } else {
                // If sign up fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
            }
        }
}

