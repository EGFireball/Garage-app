package com.idimi.garage

import com.google.common.truth.Truth.assertThat
import com.idimi.garage.datamodel.model.FuelType
import com.idimi.garage.datamodel.model.Vehicle
import org.junit.jupiter.api.Test


class VehicleDataClassTest {

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
}