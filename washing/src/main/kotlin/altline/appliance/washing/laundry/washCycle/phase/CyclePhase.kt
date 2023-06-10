package altline.appliance.washing.laundry.washCycle.phase

import io.nacular.measured.units.*

interface CyclePhase {
    val sections: List<PhaseSection>
    val duration: Measure<Time>
}

interface PhaseSection {
    /** Programmed or estimated duration of the section. The duration is estimated (as opposed to programmed) in cases
     * where it depends on outside factors such as liquid flow rate. */
    val duration: Measure<Time>

    /** Delay at the end of the section. */
    val endDelay: Measure<Time>
}