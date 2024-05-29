package com.gultekinahmetabdullah.softedu.leaderboard

data class User(
    var id: String = "",
    val name: String = "",
    val surname: String = "",
    val nickname: String = "",
    val score: Int = 0,
    val experienceLevel: Int = 1
)