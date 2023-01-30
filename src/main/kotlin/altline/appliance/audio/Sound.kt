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

    TumbleStart("tumble_start"),
    TumbleStop("tumble_stop"),
    TumbleLoop("tumble_loop");

    val clip: AudioCue = getSound(fileName, polyphony)

    private fun getSound(name: String, polyphony: Int): AudioCue {
        return AudioCue.makeStereoCue(javaClass.getResource("/sounds/$name.wav"), polyphony)
    }
}