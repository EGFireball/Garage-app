package com.idimi.garage

import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idimi.garage.api.GarageAPI
import com.idimi.garage.datamodel.RoomDB
import com.idimi.garage.datamodel.dao.VehicleDAO
import com.idimi.garage.datamodel.model.FuelType
import com.idimi.garage.datamodel.model.Vehicle
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

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class VehicleViewTests {

    val api: GarageAPI = mockk()
    val db: RoomDB = mockk()
    val vehicleDao = mockk<VehicleDAO>()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

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

            composeTestRule.setContent {
                AddVehiclePopup(
                    garageViewModel = viewModel,
                    vehicleForEdit = null,
                ) {

                }
            }

            composeTestRule.onNodeWithTag("addVehicleSubmit")
                .assertIsNotEnabled()

            composeTestRule.onNodeWithTag("addVehicleCancel")
                .assertIsEnabled()

            composeTestRule.onNodeWithTag("vehicleName")
                .performTextInput(testVehicle.name)

            composeTestRule.onNode(
                hasTestTag("vehicleName") and hasSetTextAction()
            ).assertTextEquals(testVehicle.name)

            composeTestRule.onNodeWithTag("addVehicleSubmit")
                .assertIsNotEnabled()

            composeTestRule.onNodeWithTag("vehicleMake")
                .performTextInput(testVehicle.make)

            composeTestRule.onNode(
                hasTestTag("vehicleMake") and hasSetTextAction()
            ).assertTextEquals(testVehicle.make)

            composeTestRule.onNodeWithTag("addVehicleSubmit")
                .assertIsNotEnabled()

            composeTestRule.onNodeWithTag("vehicleModel")
                .performTextInput(testVehicle.model)

            composeTestRule.onNode(
                hasTestTag("vehicleModel") and hasSetTextAction()
            ).assertTextEquals(testVehicle.model)

            composeTestRule.onNodeWithTag("addVehicleSubmit")
                .assertIsNotEnabled()

            composeTestRule.onNodeWithTag("vehicleVIN")
                .performTextInput(testVehicle.vin)

            composeTestRule.onNode(
                hasTestTag("vehicleVIN") and hasSetTextAction()
            ).assertTextEquals(testVehicle.vin)

            composeTestRule.onNodeWithTag("addVehicleSubmit")
                .assertIsNotEnabled()

            // Click to expand dropdown
            composeTestRule
                .onNodeWithTag("yearDropdown")
                .performClick()

            // Select an option (assuming options have text)
            composeTestRule
                .onNodeWithText("2023")
                .performClick()

            // Assert the selected option is displayed
            composeTestRule
                .onNodeWithText("2023")
                .assertIsDisplayed()

            composeTestRule.onNodeWithTag("addVehicleSubmit")
                .assertIsNotEnabled()

            // Click to expand dropdown
            composeTestRule
                .onNodeWithTag("fuelDropdown")
                .performClick()

            // Select an option (assuming options have text)
            composeTestRule
                .onNodeWithText("Electric")
                .performClick()

            // Assert the selected option is displayed
            composeTestRule
                .onNodeWithText("Electric")
                .assertIsDisplayed()

            composeTestRule.onNodeWithTag("addVehicleSubmit")
                .assertIsEnabled()
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

            composeTestRule.setContent {
                AddVehiclePopup(
                    garageViewModel = viewModel,
                    vehicleForEdit = testVehicle,
                ) {

                }
            }

            composeTestRule.onNodeWithTag("vehicleIcon")
                .assertExists()
                .assertIsDisplayed()

            composeTestRule.onNodeWithText(testVehicle.name)
                .assertExists()
                .assertIsDisplayed()

            composeTestRule.onNodeWithText(testVehicle.make)
                .assertExists()
                .assertIsDisplayed()

            composeTestRule.onNodeWithText(testVehicle.model)
                .assertExists()
                .assertIsDisplayed()

            composeTestRule.onNodeWithText(testVehicle.vin)
                .assertExists()
                .assertIsDisplayed()

            composeTestRule
                .onNodeWithText(testVehicle.fuelType.name.lowercase().replaceFirstChar { it.uppercase() })//("Hybrid")
                .assertIsDisplayed()
        }
    }
}