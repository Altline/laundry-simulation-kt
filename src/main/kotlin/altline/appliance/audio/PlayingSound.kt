package altline.appliance.audio

sealed interface PlayingSound {
    class Single(
        val sound: Sound,
        val instanceId: Int
    ) : PlayingSound

    class Set(
        val soundSet: SoundSet,
    ) : PlayingSound
}