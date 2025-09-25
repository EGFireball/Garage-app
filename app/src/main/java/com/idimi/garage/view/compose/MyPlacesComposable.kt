package com.idimi.garage.view.compose

import android.content.Intent
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.idimi.garage.datamodel.model.Place
import com.idimi.garage.util.isNetworkAvailable
import com.idimi.garage.view.compose.components.EnhancedRatingBar
import com.idimi.garage.view.compose.components.MapElement
import com.idimi.garage.view.compose.util.BuildImageLoader
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idimi.garage.view.compose.components.CustomIconSwitch
import com.idimi.garage.view.ui.theme.NonFavorite
import com.idimi.garage.view.ui.theme.getTheme
import com.idimi.garage.view.viewmodel.GarageViewModel
import kotlinx.coroutines.launch

@Composable
fun PlacesScreen(
    modifier: Modifier,
    garageViewModel: GarageViewModel,
)
{

    val items = garageViewModel.placesStateFlow.collectAsStateWithLifecycle()

    val isFavoriteSelected = remember {
        mutableStateOf(false)
    }

    val triggerListRecomposition = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(24.dp))
            FilterChip(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .align(Alignment.CenterVertically),
                colors = SelectableChipColors(
                    containerColor = getTheme().primaryContainer,
                    labelColor = getTheme().onSecondary,
                    selectedContainerColor = getTheme().tertiary,
                    selectedLabelColor = getTheme().onPrimary,
                    leadingIconColor = Color.Transparent,
                    trailingIconColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledLabelColor = Color.Transparent,
                    disabledLeadingIconColor = Color.Transparent,
                    disabledTrailingIconColor = Color.Transparent,
                    disabledSelectedContainerColor = Color.Transparent,
                    selectedLeadingIconColor = Color.Transparent,
                    selectedTrailingIconColor = Color.Transparent
                ),
                selected = !isFavoriteSelected.value,
                onClick = {
                    garageViewModel.showAllPlaces()
                    triggerListRecomposition.value = true
                    isFavoriteSelected.value = false
                },
                label = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "All",
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                },
            )
            Spacer(Modifier.width(24.dp))
            FilterChip(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .align(Alignment.CenterVertically),
                colors = SelectableChipColors(
                    containerColor = getTheme().primaryContainer,
                    labelColor = getTheme().onSecondary,
                    selectedContainerColor = getTheme().tertiary,
                    selectedLabelColor = getTheme().onPrimary,
                    leadingIconColor = Color.Transparent,
                    trailingIconColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledLabelColor = Color.Transparent,
                    disabledLeadingIconColor = Color.Transparent,
                    disabledTrailingIconColor = Color.Transparent,
                    disabledSelectedContainerColor = Color.Transparent,
                    selectedLeadingIconColor = Color.Transparent,
                    selectedTrailingIconColor = Color.Transparent
                ),
                selected = isFavoriteSelected.value,
                onClick = {
                    garageViewModel.showOnlyFavoritePlaces()
                    triggerListRecomposition.value = true
                    isFavoriteSelected.value = true
                },
                label = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Favorites",
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                },
            )
            Spacer(Modifier.width(24.dp))
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(items.value) { item ->
                PlaceCard(
                    garageViewModel = garageViewModel,
                    place = item,
                    triggerRecomposition = triggerListRecomposition
                )
            }
        }
    }
}

@Composable
fun PlaceCard(
    modifier: Modifier = Modifier,
    garageViewModel: GarageViewModel,
    place: Place,
    initiallyExpanded: Boolean = false,
    triggerRecomposition: MutableState<Boolean>
) {

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    var expanded by rememberSaveable {
        mutableStateOf(initiallyExpanded)
    }

    val iconRotation by animateFloatAsState(
        if (expanded) 180f else 0f, tween(durationMillis = 300)
    )

    val elevation by animateDpAsState(
        targetValue = if (expanded) 8.dp else 2.dp
    )

    val isFavorite = remember {
        mutableStateOf(place.isFavorite)
    }

    var showDefault by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = Unit, key2 = triggerRecomposition.value) {
        garageViewModel.isItemFavorite(place) { favorite ->
            isFavorite.value = favorite
            triggerRecomposition.value = false
        }
    }

    val imageLoader = remember { BuildImageLoader.imageLoader }

    val imageRequest =
        with(ImageRequest.Builder(context)) {
            data(place.url320)
            if (isNetworkAvailable(context)) {
                networkCachePolicy(CachePolicy.ENABLED)
            } else {
                diskCachePolicy(CachePolicy.ENABLED)
            }
                .build()
        }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(if (expanded) 520.dp else 200.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("placeCard")
            .clickable {
                expanded = !expanded
            },
        colors = CardColors(
            contentColor = getTheme().onPrimary,
            containerColor = getTheme().background,
            disabledContentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier.size(120.dp)
                ) {
                    if (showDefault) {
                        Icon(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(
                                    shape = RoundedCornerShape(
                                        corner = CornerSize(16.dp)
                                    )
                                ),
                            imageVector = Icons.Default.LocationOn,
                            tint = getTheme().onPrimary,
                            contentDescription = "Default Cover"
                        )
                    }
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(
                                shape = RoundedCornerShape(
                                    corner = CornerSize(16.dp)
                                )
                            ),
                        model = imageRequest,
                        imageLoader = imageLoader,
                        contentDescription = "COVER",
                        contentScale = ContentScale.FillBounds,
                        onSuccess = {
                            showDefault = false
                        },
                        onLoading = {
                            showDefault = true
                        },
                        onError = {
                            showDefault = true
                        }
                    )
                }
                Spacer(Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier.testTag("placeName"),
                        text = place.name,
                        fontSize = 16.sp
                    )
                    Text(
                        text = place.primaryCategoryDisplayName,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    EnhancedRatingBar(
                        rating = place.rating
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                CustomIconSwitch(
                    modifier = Modifier
                        .width(60.dp)
                        .wrapContentHeight(),
                    checked = isFavorite.value,
                    filledIcon = Icons.Filled.Star,
                    outlinedIcon = Icons.Outlined.Star,
                    iconColor = if(isFavorite.value) Color.Yellow else NonFavorite,
                    onCheckedChange = { isChecked ->
                        coroutineScope.launch {
                            garageViewModel.changeFavoriteStateForPlace(
                                isChecked,
                                place
                            ) { favorite ->
                                isFavorite.value = favorite
                            }
                        }
                    },
                    colors = SwitchColors(
                        checkedTrackColor = getTheme().primaryContainer,
                        uncheckedTrackColor = getTheme().secondaryContainer,
                        checkedThumbColor = getTheme().primaryContainer,
                        uncheckedThumbColor = getTheme().secondaryContainer,
                        checkedBorderColor = Color.Transparent,
                        uncheckedBorderColor = Color.Transparent,
                        uncheckedIconColor = Color.Transparent,
                        disabledCheckedIconColor = Color.Transparent,
                        disabledCheckedThumbColor = Color.Transparent,
                        disabledCheckedBorderColor = Color.Transparent,
                        disabledUncheckedTrackColor = Color.Transparent,
                        disabledCheckedTrackColor = Color.Transparent,
                        checkedIconColor = Color.Transparent,
                        disabledUncheckedThumbColor = Color.Transparent,
                        disabledUncheckedBorderColor = Color.Transparent,
                        disabledUncheckedIconColor = Color.Transparent,
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            if (expanded) {
                // Expandable content
                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically(tween(durationMillis = 500)),
                    exit = shrinkVertically(tween(durationMillis = 500))
                ) {
                    MapElement(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp),
                        lat = place.latitude,
                        lng = place.longtitude,
                        markerTitle = place.name,
                    ) { marker ->
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                place.url.toUri()
                            )
                        )
                        true
                    }
                }
            }
        }
    }
}