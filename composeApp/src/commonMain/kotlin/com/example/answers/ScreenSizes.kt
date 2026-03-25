package com.example.answers

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ScreenSizes(
    val titleSize: TextUnit,
    val inputTextSize: TextUnit,
    val buttonTextSize: TextUnit,
    val inputHeight: Dp,
    val buttonHeight: Dp,
    val padding: Dp,
    val fieldWidth: Float,
    val verticalSpacing: Dp,
    val sectionSpacing: Dp
)

fun getAdaptiveSizes(screenWidthDp: Float): ScreenSizes {
    return when {
        screenWidthDp > 900 -> ScreenSizes(
            titleSize = 52.sp,
            inputTextSize = 36.sp,
            buttonTextSize = 32.sp,
            inputHeight = 80.dp,
            buttonHeight = 80.dp,
            padding = 48.dp,
            fieldWidth = 0.6f,
            verticalSpacing = 32.dp,
            sectionSpacing = 48.dp
        )
        screenWidthDp > 600 -> ScreenSizes(
            titleSize = 44.sp,
            inputTextSize = 24.sp,
            buttonTextSize = 28.sp,
            inputHeight = 70.dp,
            buttonHeight = 70.dp,
            padding = 32.dp,
            fieldWidth = 0.7f,
            verticalSpacing = 24.dp,
            sectionSpacing = 40.dp
        )
        else -> ScreenSizes(
            titleSize = 36.sp,
            inputTextSize = 28.sp,
            buttonTextSize = 24.sp,
            inputHeight = 60.dp,
            buttonHeight = 60.dp,
            padding = 16.dp,
            fieldWidth = 0.9f,
            verticalSpacing = 8.dp,
            sectionSpacing = 8.dp
        )
    }
}