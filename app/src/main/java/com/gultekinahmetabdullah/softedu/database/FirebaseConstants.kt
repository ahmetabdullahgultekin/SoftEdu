package com.gultekinahmetabdullah.softedu.database

object FirestoreConstants {
    const val COLLECTION_QUESTIONS = "questions"
    const val COLLECTION_USERS = "users"
    const val COLLECTION_FEEDBACKS = "feedbacks"
    const val COLLECTION_APKS = "apks"
    const val FIELD_DIFFICULTY_LEVEL = "difficultyLevel"
    const val FIELD_QUESTION_TEXT = "questionText"
    const val FIELD_CHOICES = "choices"
    const val FIELD_CORRECT_CHOICE = "correctChoice"
    const val FIELD_CORRECT_QUESTIONS = "correctQuestions"
    const val FIELD_WRONG_QUESTIONS = "wrongQuestions"
    const val FIELD_SCORE = "score"
    const val FIELD_USER_ID = "userId"
    const val FIELD_FEEDBACK = "feedback"
    const val FIELD_TIMESTAMP = "timestamp"
    const val FIELD_EMAIL = "email"
    const val FIELD_NAME = "name"
    const val FIELD_SURNAME = "surname"
    const val FIELD_NICKNAME = "nickname"
    const val FIELD_EXPERIENCE_LEVEL = "experienceLevel"
    const val FIELD_LATEST = "latest"
    const val FIELD_HASH_CODE = "hashCode"
    const val FIELD_SHORT_LINK = "shortLink"


    const val LIMIT_ANNOUNCEMENTS: Long = 50
    const val LIMIT_QUESTIONS: Long = 10
    const val INCREASE_SCORE: Long = 10
}