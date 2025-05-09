package com.example.clasesparticularesapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.clasesparticularesapp.R

val SFUIDisplay = FontFamily(Font(R.font.sanfrancisco_bold))

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = SFUIDisplay,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
        titleLarge = TextStyle(
        fontFamily = SFUIDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
))


