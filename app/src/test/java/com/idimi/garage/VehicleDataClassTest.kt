package com.idimi.garage

import com.google.common.truth.Truth.assertThat
import com.idimi.garage.api.GarageAPI
import com.idimi.garage.datamodel.RoomDB
import com.idimi.garage.datamodel.dao.PlaceDAO
import com.idimi.garage.datamodel.dao.VehicleDAO
import com.idimi.garage.datamodel.model.FuelType
import com.idimi.garage.datamodel.model.Vehicle
import com.idimi.garage.repo.GarageRepo
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test


class VehicleDataClassTest {

    val api: GarageAPI = mockk()
    val db: RoomDB = mockk()
    val vehicleDao = mockk<VehicleDAO>()

    @Test
    fun createVehicleInstance() {
        val testVehicle = Vehicle(
            id = 0,
            name = "Test Honda",
            make = "Honda Motor Company",
            model = "Civic TypeR",
            vin = "2H4B674560",
            year = 2022,
            fuelType = FuelType.PETROL,
            iconURL = ""
        )

        assertThat(testVehicle.name).isEqualTo("Test Honda")
        assertThat(testVehicle.model).isEqualTo("Civic TypeR")
        assertThat(testVehicle.make).isEqualTo("Honda Motor Company")
        assertThat(testVehicle.vin).isEqualTo("2H4B674560")
        assertThat(testVehicle.year).isEqualTo(2022)
        assertThat(testVehicle.fuelType).isEqualTo(FuelType.PETROL)

    }

    @Test
    fun persistVehicleInstanceIntoDatabase() {
        runTest {
            val testVehicle = Vehicle(
                id = 0,
                name = "Test Honda",
                make = "Honda Motor Company",
                model = "Civic TypeR",
                vin = "2H4B674560",
                year = 2022,
                fuelType = FuelType.PETROL,
                iconURL = ""
            )

            val repo = GarageRepo(api, db)

            every { db.getVehicleDao() } returns vehicleDao

            coEvery { vehicleDao.insert(testVehicle) } returns testVehicle.id.toLong()
            coEvery { vehicleDao.getAllVehicles() } returns flowOf(listOf(testVehicle))

            repo.setVehicle(vehicle = testVehicle, forEdit = false)

            repo.getAllVehicles().collect { vehicles ->
                assertThat(vehicles.size).isEqualTo(1)
                assertThat(vehicles[0].name).isEqualTo("Test Honda")
                assertThat(vehicles[0].model).isEqualTo("Civic TypeR")
                assertThat(vehicles[0].make).isEqualTo("Honda Motor Company")
                assertThat(vehicles[0].vin).isEqualTo("2H4B674560")
                assertThat(vehicles[0].year).isEqualTo(2022)
                assertThat(vehicles[0].fuelType).isEqualTo(FuelType.PETROL)
            }
        }
    }
}