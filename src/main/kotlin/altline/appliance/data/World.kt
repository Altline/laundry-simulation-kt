package altline.appliance.data

import altline.appliance.common.Body
import altline.appliance.common.DefaultAmbientTemperature
import altline.appliance.electricity.transit.InfiniteElectricalSource
import altline.appliance.fabric.Bedsheet
import altline.appliance.fabric.Fabric
import altline.appliance.fabric.Rag
import altline.appliance.fabric.Towel
import altline.appliance.fabric.clothing.*
import altline.appliance.measure.Temperature.Companion.celsius
import altline.appliance.measure.Volume
import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.measure.Volume.Companion.milliliters
import altline.appliance.measure.kilowatts
import altline.appliance.measure.repeatPeriodicallySpeedAware
import altline.appliance.substance.CommonSubstanceTypes
import altline.appliance.substance.MutableSubstance
import altline.appliance.substance.SoakableBody
import altline.appliance.substance.transit.Reservoir
import altline.appliance.washing.laundry.HouseholdLaundryWasher
import altline.appliance.washing.laundry.HouseholdLaundryWasherFactory
import altline.appliance.washing.laundry.HouseholdLaundryWasherScanner
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class World {

    val ambientTemperature = DefaultAmbientTemperature

    val washer: HouseholdLaundryWasher = createWasher()
    val laundry: Set<Body> = createLaundry()

    init {
        GlobalScope.launch {
            repeatPeriodicallySpeedAware(10 * seconds) {
                laundry.filterNot { it in washer.load }
                    .filterIsInstance<SoakableBody>()
                    .forEach { it.dry(0.0001 * it.volume) }
            }
        }
    }
}

private fun createWasher(): HouseholdLaundryWasher {
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
    val washer = HouseholdLaundryWasherFactory().createHouseholdLaundryWasher()
    washer.fluidIntake.inputs[0] connectTo waterSource.outputs[0]
    washer.fluidOutlet.outputs[0] connectTo fluidDrain.inputs[0]
    powerSource.outputs[0] connectTo washer.powerInlet
    washer.scanner = HouseholdLaundryWasherScanner()
    return washer
}

private fun createLaundry(): Set<Body> {
    fun MutableSet<Body>.addFabricSet(
        mediumVolume: Measure<Volume>,
        volumeDifference: Measure<Volume>,
        builder: (Clothing.Size, Measure<Volume>) -> Fabric
    ) {
        add(builder(Clothing.Size.XXS, mediumVolume - 3 * volumeDifference))
        add(builder(Clothing.Size.XS, mediumVolume - 2 * volumeDifference))
        add(builder(Clothing.Size.S, mediumVolume - 1 * volumeDifference))
        add(builder(Clothing.Size.M, mediumVolume))
        add(builder(Clothing.Size.L, mediumVolume + 1 * volumeDifference))
        add(builder(Clothing.Size.XL, mediumVolume + 2 * volumeDifference))
        add(builder(Clothing.Size.XXL, mediumVolume + 3 * volumeDifference))
    }

    return buildSet {
        addFabricSet(
            mediumVolume = 25 * milliliters,
            volumeDifference = 5 * milliliters
        ) { size, volume -> Sock(size, volume) }

        addFabricSet(
            mediumVolume = 100 * milliliters,
            volumeDifference = 10 * milliliters
        ) { size, volume -> Shirt(size, volume) }

        addFabricSet(
            mediumVolume = 480 * milliliters,
            volumeDifference = 20 * milliliters
        ) { size, volume -> Pants(size, volume) }

        addFabricSet(
            mediumVolume = 600 * milliliters,
            volumeDifference = 30 * milliliters
        ) { size, volume -> Dress(size, volume) }

        addFabricSet(
            mediumVolume = 50 * milliliters,
            volumeDifference = 5 * milliliters
        ) { _, volume -> Rag(volume) }

        addFabricSet(
            mediumVolume = 400 * milliliters,
            volumeDifference = 100 * milliliters
        ) { _, volume -> Towel(volume) }

        addFabricSet(
            mediumVolume = 700 * milliliters,
            volumeDifference = 100 * milliliters
        ) { _, volume -> Bedsheet(volume) }
    }.apply {
        forEachIndexed { index, body ->
            val substanceTypes = listOf(
                CommonSubstanceTypes.MUD,
                CommonSubstanceTypes.COFFEE,
                CommonSubstanceTypes.KETCHUP,
                CommonSubstanceTypes.CRUDE_OIL
            )
            val amountPercentages = listOf(0.1, 0.15, 0.25)

            val substanceType = substanceTypes[index % substanceTypes.size]
            val amountPercentage = amountPercentages[index % amountPercentages.size]
            body.stain(
                MutableSubstance(
                    type = substanceType,
                    amount = amountPercentage * body.volume,
                    temperature = DefaultAmbientTemperature
                )
            )
        }
    }
}