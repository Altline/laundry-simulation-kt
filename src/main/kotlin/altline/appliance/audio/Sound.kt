package altline.appliance.audio

import com.adonax.audiocue.AudioCue

enum class Sound(
    fileName: String,
    polyphony: Int = 1
) {

    PowerOn("power_on"),
    PowerOff("power_off"),

    OptionHigh("option_high", 3),
    OptionLow("option_low", 3),
    OptionPositive("option_positive", 3),
    OptionNegative("option_negative", 3),
    OptionDenied("option_denied", 3),

    CycleSelect("cycle_select", 2),
    CycleSelectOff("cycle_select_off", 2),

    DispenserOpen("dispenser_open"),
    DispenserClose("dispenser_close"),

    ThickLiquidAdd("thick_liquid_add", 3),
    ThickLiquidRemove("thick_liquid_remove", 3),
    ThinLiquidAdd("thin_liquid_add", 3),
    ThinLiquidRemove("thin_liquid_remove", 3),

    TumbleStart("tumble_start"),
    TumbleStop("tumble_stop"),
    TumbleLoop("tumble_loop");

    val clip: AudioCue = getSound(fileName, polyphony)

    private fun getSound(name: String, polyphony: Int): AudioCue {
        return AudioCue.makeStereoCue(javaClass.getResource("/sounds/$name.wav"), polyphony)
    }
}