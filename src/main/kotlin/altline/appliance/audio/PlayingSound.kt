package altline.appliance.audio

sealed interface PlayingSound {
    class Single(
        val sound: SoundClip,
        val instanceId: Int
    ) : PlayingSound

    class Set(
        val soundSet: SoundSet,
    ) : PlayingSound
}