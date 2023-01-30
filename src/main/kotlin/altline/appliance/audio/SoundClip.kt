package altline.appliance.audio

import com.adonax.audiocue.AudioCue

enum class SoundClip(
    fileName: String,
    polyphony: Int = 1
) : Sound {
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

    PreFillStart("pre_fill_start"),
    PreFillStop("pre_fill_stop"),
    PreFillLoop("pre_fill_loop"),

    MainFillStart("main_fill_start"),
    MainFillStop("main_fill_stop"),
    MainFillLoop("main_fill_loop"),

    SoftenerFillStart("softener_fill_start"),
    SoftenerFillStop("softener_fill_stop"),
    SoftenerFillLoop("softener_fill_loop"),

    TumbleStart("tumble_start"),
    TumbleStop("tumble_stop"),
    TumbleLoop("tumble_loop");

    val cue: AudioCue = getSound(fileName, polyphony)

    private fun getSound(name: String, polyphony: Int): AudioCue {
        return AudioCue.makeStereoCue(javaClass.getResource("/sounds/$name.wav"), polyphony)
    }
}