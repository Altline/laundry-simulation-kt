package altline.appliance.data

import altline.appliance.common.Body
import altline.appliance.electricity.transit.InfiniteElectricalSource
import altline.appliance.fabric.Clothing
import altline.appliance.fabric.Shirt
import altline.appliance.measure.Temperature.Companion.celsius
import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.measure.Volume.Companion.milliliters
import altline.appliance.measure.kilowatts
import altline.appliance.substance.CommonSubstanceTypes
import altline.appliance.substance.MutableSubstance
import altline.appliance.substance.transit.Reservoir
import altline.appliance.washing.laundry.HouseholdLaundryWasher
import altline.appliance.washing.laundry.HouseholdLaundryWasherFactory
import altline.appliance.washing.laundry.HouseholdLaundryWasherScanner
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

class World {

    val washer: HouseholdLaundryWasher
    val laundry: Set<Body>

    init {
        laundry = setOf(
            Shirt(Clothing.Size.S, 80 * milliliters).apply {
                soak(MutableSubstance(CommonSubstanceTypes.WATER, 30 * milliliters, 20 * celsius))
                stain(MutableSubstance(CommonSubstanceTypes.COFFEE, 200 * milliliters, 20 * celsius))
            },
            Shirt(Clothing.Size.M, 100 * milliliters).apply {
                soak(MutableSubstance(CommonSubstanceTypes.WATER, 80 * milliliters, 20 * celsius))
                stain(MutableSubstance(CommonSubstanceTypes.COFFEE, 20 * milliliters, 20 * celsius))
            },
            Shirt(Clothing.Size.L, 120 * milliliters).apply {
                soak(MutableSubstance(CommonSubstanceTypes.WATER, 30 * milliliters, 20 * celsius))
                stain(MutableSubstance(CommonSubstanceTypes.COFFEE, 10 * milliliters, 20 * celsius))
            },
        )

        val powerSource = InfiniteElectricalSource(10 * kilowatts)

        val waterSource = Reservoir(
            capacity = 1000000 * liters,
            outflowThreshold = 0 * liters,
            shouldSpontaneouslyPush = true,
            inputFlowRate = 0 * liters / seconds,
            outputFlowRate = 100 * liters / seconds,
            initialSubstance = MutableSubstance(CommonSubstanceTypes.WATER, 1000000 * liters, 20 * celsius)
        )
        val fluidDrain = Reservoir(
            capacity = 1000000 * liters,
            outflowThreshold = 0 * liters,
            shouldSpontaneouslyPush = false,
            inputFlowRate = 100 * liters / seconds,
            outputFlowRate = 0 * liters / seconds
        )

        washer = HouseholdLaundryWasherFactory().createHouseholdLaundryWasher()

        washer.fluidIntake.inputs[0] connectTo waterSource.outputs[0]
        washer.fluidOutlet.outputs[0] connectTo fluidDrain.inputs[0]

        powerSource.outputs[0] connectTo washer.powerInlet

        washer.load(laundry.first())

        washer.scanner = HouseholdLaundryWasherScanner()
    }
}