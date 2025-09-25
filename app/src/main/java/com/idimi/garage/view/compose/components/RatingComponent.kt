package com.idimi.garage.view.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun EnhancedRatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    onRatingChange: (Float) -> Unit = {},
    starSize: Dp = 24.dp,
    maxRating: Int = 5,
    isReadOnly: Boolean = true,
    spacing: Dp = 4.dp,
    ratingColor: Color = Color.Yellow,
    backgroundColor: Color = Color.Gray,
    filledStarImage: ImageVector = Icons.Filled.Star,  // Param for filled star image
    emptyStarImage: ImageVector = Icons.Outlined.Star,
    onClickRating: (Float) -> Unit = {},
) {
    // Synchronize `currentRating` with `rating` dynamically
    var currentRating by remember(rating) { mutableStateOf(rating) }

    Row(
        modifier = modifier.wrapContentSize().testTag("ratingBar"),
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        (1..maxRating).forEach { step ->
            val isFilled = currentRating >= step
            RatingStar(
                isFilled = isFilled,
                starSize = starSize,
                filledStarColor = ratingColor,
                emptyStarColor = backgroundColor,
                emptyStarImage = emptyStarImage,
                filledStarImage = filledStarImage,
//                onClick = {
//                    if (!isReadOnly) {
//                        // Toggle the rating for the first star
//                        currentRating = if (step == currentRating.toInt() && step == 1) {
//                            0f // Deselect if the first star is tapped again
//                        } else {
//                            step.toFloat() // Set rating to the tapped star
//                        }
//                        onRatingChange(currentRating)
//                    } else {
//                        onClickRating(step.toFloat())
//                    }
//                }
            )
        }
    }
}

@Composable
private fun RatingStar(
    isFilled: Boolean,
    starSize: Dp,
    filledStarColor: Color,
    emptyStarColor: Color,
    isReadOnly: Boolean = false,
    filledStarImage: ImageVector = Icons.Filled.Star,  // Param for filled star image
    emptyStarImage: ImageVector = Icons.Outlined.Star,
    onClick: () -> Unit = {}
) {
    Image(
        imageVector = if (isFilled) filledStarImage else emptyStarImage,
        contentDescription = "Star",
        colorFilter = ColorFilter.tint(if (isFilled) filledStarColor else emptyStarColor),
        modifier = Modifier
            .size(starSize)
            .then(
//                if (!isReadOnly) {
//                    Modifier.clickable(
//                        onClick = onClick,
//                        indication = null,
//                        interactionSource = remember { MutableInteractionSource() }
//                    )
//                } else {
                Modifier // No clickable behavior
//                }
            )
    )
}