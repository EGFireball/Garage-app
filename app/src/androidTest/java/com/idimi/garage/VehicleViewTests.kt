package com.idimi.garage

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idimi.garage.api.GarageAPI
import com.idimi.garage.datamodel.RoomDB
import com.idimi.garage.datamodel.dao.VehicleDAO
import com.idimi.garage.datamodel.model.FuelType
import com.idimi.garage.datamodel.model.Vehicle
import com.idimi.garage.repo.GarageRepo
import com.idimi.garage.view.compose.AddVehiclePopup
import com.idimi.garage.view.viewmodel.GarageViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class VehicleViewTests {

    val api: GarageAPI = mockk()
    val db: RoomDB = mockk()
    val vehicleDao = mockk<VehicleDAO>()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composableTestRule = createComposeRule()

    @Before
    fun before() {
        hiltRule.inject()
    }

    @Test
    fun checkAddVehiclePopup() {
        runTest {

            val viewModel: GarageViewModel = mockk()

            val testVehicle = Vehicle(
                id = 0,
                name = "Test Honda",
                make = "Honda Motor Company",
                model = "Civic TypeR",
                vin = "2H4B674560",
                year = 2022,
                fuelType = FuelType.HYBRID,
                iconURL = ""
            )

            every { db.getVehicleDao() } returns vehicleDao

            composableTestRule.setContent {
                AddVehiclePopup(
                    garageViewModel = viewModel,
                    vehicleForEdit = null,
                ) {

                }
            }

            composableTestRule.onNodeWithTag("vehicleName")
                .performTextInput(testVehicle.name)

            composableTestRule.onNode(
                hasTestTag("vehicleName") and hasSetTextAction()
            ).assertTextEquals(testVehicle.name)

            composableTestRule.onNodeWithTag("vehicleMake")
                .performTextInput(testVehicle.make)

            composableTestRule.onNode(
                hasTestTag("vehicleMake") and hasSetTextAction()
            ).assertTextEquals(testVehicle.make)

            composableTestRule.onNodeWithTag("vehicleModel")
                .performTextInput(testVehicle.model)

            composableTestRule.onNode(
                hasTestTag("vehicleModel") and hasSetTextAction()
            ).assertTextEquals(testVehicle.model)

            composableTestRule.onNodeWithTag("vehicleVIN")
                .performTextInput(testVehicle.vin)

            composableTestRule.onNode(
                hasTestTag("vehicleVIN") and hasSetTextAction()
            ).assertTextEquals(testVehicle.vin)


        }
    }

    @Test
    fun checkEditVehiclePopup() {
        runTest {

            val viewModel: GarageViewModel = mockk()

            val testVehicle = Vehicle(
                id = 0,
                name = "Test Honda",
                make = "Honda Motor Company",
                model = "Civic TypeR",
                vin = "2H4B674560",
                year = 2022,
                fuelType = FuelType.HYBRID,
                iconURL = ""
            )

            every { db.getVehicleDao() } returns vehicleDao

            composableTestRule.setContent {
                AddVehiclePopup(
                    garageViewModel = viewModel,
                    vehicleForEdit = testVehicle,
                ) {

                }
            }

            composableTestRule.onNodeWithTag("vehicleIcon")
                .assertExists()
                .assertIsDisplayed()

            composableTestRule.onNodeWithText(testVehicle.name)
                .assertExists()
                .assertIsDisplayed()

            composableTestRule.onNodeWithText(testVehicle.make)
                .assertExists()
                .assertIsDisplayed()

            composableTestRule.onNodeWithText(testVehicle.model)
                .assertExists()
                .assertIsDisplayed()

            composableTestRule.onNodeWithText(testVehicle.vin)
                .assertExists()
                .assertIsDisplayed()
        }
    }
}