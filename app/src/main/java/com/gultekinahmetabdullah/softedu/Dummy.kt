package com.gultekinahmetabdullah.softedu

import androidx.annotation.DrawableRes

data class Lib(@DrawableRes val icon: Int, val name:String)

val libraries = listOf<Lib>(
    Lib(R.drawable.ic_temporary, "Learnlist"),
    Lib(R.drawable.ic_temporary,"Artists"),
    Lib(R.drawable.ic_temporary,"Album"),
    Lib(R.drawable.ic_temporary,"Songs"),
    Lib(R.drawable.ic_temporary,"Genre")
)