package com.gultekinahmetabdullah.softedu.database

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


//addQuestionToFirestore(2,"questionText", listOf("choice1", "choice2", "choice3"), 1)
fun addQuestionToFirestore(difficultyLevel: Int,
                           questionText: String,
                           choices: List<String>,
                           correctChoice: Int) {
    val db = Firebase.firestore
    // Create a new question with a difficulty level, question text, choices, and correct choice

    val question = hashMapOf(
        "difficultyLevel" to difficultyLevel,
        "questionText" to questionText,
        "choices" to choices,
        "correctChoice" to correctChoice
    )

    // Add a new document with a generated ID to the "questions" collection
    db.collection("questions")
            .add(question)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
}


fun updateAnsweredQuestions(userId: String, questionId: String, isCorrect: Boolean) {
    val db = Firebase.firestore

    if (isCorrect) {
        // If the answer is correct, add the question ID to the correctQuestions array
        db.collection("users")
                .document(userId)
                .update("correctQuestions", FieldValue.arrayUnion(questionId),
                        "wrongQuestions", FieldValue.arrayRemove(questionId),
                        "score", FieldValue.increment(10))


    } else {
        // If the answer is incorrect, add the question ID to the wrongQuestions array
        db.collection("users")
                .document(userId)
                .update("wrongQuestions", FieldValue.arrayUnion(questionId))
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
                    println("Sign up successful. User ID is ${user?.uid}")

                    // Create a new user document in Firestore
                    val userProfile = hashMapOf(
                        "name" to name,
                        "surname" to surname,
                        "experienceLevel" to experienceLevel,
                        "score" to 0
                    )

                    user?.uid?.let { uid ->
                        db.collection("users")
                                .document(uid)
                                .set(userProfile)
                                .addOnSuccessListener {
                                    println("User profile created in Firestore.")
                                }
                                .addOnFailureListener { e ->
                                    println("Error creating user profile in Firestore.")
                                    e.printStackTrace()
                                }
                    }
                } else {
                    // If sign up fails, display a message to the user.
                    println("Sign up failed.")
                }
            }
}