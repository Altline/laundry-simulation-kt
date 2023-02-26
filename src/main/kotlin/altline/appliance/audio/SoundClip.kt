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

    DoorLock("door_lock"),
    DoorUnlock("door_unlock"),

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

    DrainFlowStart("drain_flow_start"),
    DrainFlowStop("drain_flow_stop"),
    DrainFlowLoop("drain_flow_loop"),

    DrainMainStart("drain_main_start"),
    DrainMainStop("drain_main_stop"),
    DrainMainLoop("drain_main_loop"),
    DrainMainTransition("drain_main_transition"),

    DrainDryStart("drain_dry_start"),
    DrainDryStop("drain_dry_stop"),
    DrainDryLoop("drain_dry_loop"),
    DrainDryTransition("drain_dry_transition"),

    TumbleStart("tumble_start"),
    TumbleStop("tumble_stop"),
    TumbleLoop("tumble_loop"),

    SpinSlowStart("spin_slow_start"),
    SpinSlowStop("spin_slow_stop"),
    SpinSlowLoop("spin_slow_loop"),

    SpinFastTransition("spin_fast_transition"),
    SpinFastStop("spin_fast_stop"),
    SpinFastLoop("spin_fast_loop");

    val cue: AudioCue = getSound(fileName, polyphony)

    private fun getSound(name: String, polyphony: Int): AudioCue {
        return AudioCue.makeStereoCue(javaClass.getResource("/sounds/$name.wav"), polyphony)
    }
}