package com.gultekinahmetabdullah.softedu.util

import androidx.annotation.DrawableRes
import com.gultekinahmetabdullah.softedu.R

data class Lib(@DrawableRes val icon: Int, val name:String)

val libraries = listOf<Lib>(
    Lib(R.drawable.ic_temporary, "Learnings"),
    Lib(R.drawable.ic_temporary,"Artists"),
    Lib(R.drawable.ic_temporary,"Album"),
    Lib(R.drawable.ic_temporary,"Songs"),
    Lib(R.drawable.ic_temporary,"Genre")
)