package com.idimi.garage.view.compose

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.idimi.garage.view.compose.components.CustomIconSwitch
import com.idimi.garage.view.viewmodel.GarageViewModel
import kotlinx.coroutines.launch


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

    LaunchedEffect(key1 = Unit, key2 = triggerRecomposition.value) {
        garageViewModel.isItemFavorite(place) { favorite ->
            isFavorite.value = favorite
            triggerRecomposition.value = false
        }
    }

    val imageLoader = remember { BuildImageLoader.imageLoader }

    val imageRequest =
        with(ImageRequest.Builder(context)) {
            data(place.url145)
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
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .clickable {
                    expanded = !expanded
                },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                AsyncImage(
                    modifier = Modifier
                        .size(120.dp)
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
                        //showDefault = false
                    },
                    onLoading = {
                        //showDefault = true
                    },
                    onError = {
                        //showDefault = true
                    }
                )
                Spacer(Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
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
                    onCheckedChange = { isChecked ->
                        coroutineScope.launch {
                            garageViewModel.changeFavoriteStateForPlace(
                                isChecked,
                                place
                            ) { favorite ->
                                isFavorite.value = favorite
                            }
                        }
                    }
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