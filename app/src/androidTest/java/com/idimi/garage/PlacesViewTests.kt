package com.idimi.garage

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idimi.garage.api.GarageAPI
import com.idimi.garage.datamodel.RoomDB
import com.idimi.garage.datamodel.dao.PlaceDAO
import com.idimi.garage.datamodel.model.Place
import com.idimi.garage.repo.GarageRepo
import com.idimi.garage.view.compose.PlaceCard
import com.idimi.garage.view.viewmodel.GarageViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PlacesViewTests {

    val api: GarageAPI = mockk()
    val db: RoomDB = mockk()
    val placeDao = mockk<PlaceDAO>()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
//    val composableTestRule = createAndroidComposeRule<MainActivity>()
    val composableTestRule = createComposeRule()

    @Before
    fun before() {
        hiltRule.inject()
    }

    @Test
    fun checkPlaceCard() {
        runBlocking {

            val mockPlace = Place(
                id = 0,
                poiId = 22606,
                name = "My Place",
                url = "",
                primaryCategoryDisplayName = "Good Holiday Place",
                rating = 5f,
                url320 = "",
                url145 = "",
                latitude = 35.0,
                longtitude = 75.0,
                isFavorite = false
            )
            every { db.getPlaceDao() } returns placeDao

            composableTestRule.setContent {
                val triggerRecomposition = remember {
                    mutableStateOf(false)
                }

                val repo = GarageRepo(api, db)
                val vm = GarageViewModel(garageRepo = repo)

                PlaceCard(
                    garageViewModel = vm,
                    place = mockPlace,
                    triggerRecomposition = triggerRecomposition
                )
            }

            composableTestRule.onNodeWithTag("placeCard")
                .assertExists()
                .assertIsDisplayed()

            composableTestRule.onNodeWithText(mockPlace.name)
                .assertExists()
                .assertIsDisplayed()

            composableTestRule.onNodeWithText(mockPlace.primaryCategoryDisplayName)
                .assertExists()
                .assertIsDisplayed()
        }
    }
}