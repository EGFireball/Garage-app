//package com.idimi.garage
//
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.assertTextEquals
//import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.lifecycle.ViewModelProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.idimi.garage.api.GarageAPI
//import com.idimi.garage.datamodel.RoomDB
//import com.idimi.garage.datamodel.model.Place
//import com.idimi.garage.repo.GarageRepo
//import com.idimi.garage.view.MainActivity
//import com.idimi.garage.view.compose.PlaceCard
//import com.idimi.garage.view.viewmodel.GarageViewModel
//import dagger.hilt.android.testing.BindValue
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import io.mockk.mockk
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import javax.inject.Inject
//
//@HiltAndroidTest
//class PlacesViewTests {
//
//    @get:Rule(order = 0)
//    val hiltRule = HiltAndroidRule(this)
//
//    @get:Rule(order = 1)
//    val composableTestRule = createAndroidComposeRule<MainActivity>()
//
//    @Inject
//    lateinit var garageViewModel: GarageViewModel
//
////    @Before
////    fun setupTest() {
////        hiltRule.inject()
////    }
//
//    @Test
//    fun checkPlaceCard() {
//
//        val mockPlace = Place(
//            id = 0,
//            poiId = 34567,
//            name = "My Place",
//            url = "",
//            primaryCategoryDisplayName = "",
//            rating = 5f,
//            url320 = "",
//            url145 = "",
//            latitude = 35.0,
//            longtitude = 75.0,
//            isFavorite = false
//        )
//        composableTestRule.setContent {
//            val triggerRecomposition = remember {
//                mutableStateOf(false)
//            }
//            PlaceCard(
//                garageViewModel = garageViewModel,
//                place = mockPlace,
//                triggerRecomposition = triggerRecomposition
//            )
//        }
////
////        composableTestRule.onNodeWithTag("placeCard")
////            .assertExists()
////            .assertIsDisplayed()
//    }
//}