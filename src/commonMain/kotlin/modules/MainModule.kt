package modules

import com.soywiz.korau.sound.PlaybackTimes
import com.soywiz.korau.sound.readMusic
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.ScaleMode
import com.soywiz.korma.geom.SizeInt
import ui.scenes.FirstScene
import ui.scenes.SecondScene
import ui.scenes.ThirdScene
import util.utils.*
import kotlin.reflect.KClass

object MainModule : Module() {
    override val mainScene: KClass<out Scene> = FirstScene::class
    override val title: String = "Metroidvania"
    override val bgcolor: RGBA = Colors.AQUAMARINE
    override val size: SizeInt = SizeInt.invoke(x = GAME_WINDOW_WIDTH, y = GAME_WINDOW_HEIGHT)
    override val windowSize: SizeInt = SizeInt.invoke(x = GAME_WINDOW_WIDTH, y = GAME_WINDOW_HEIGHT)
    override val fullscreen: Boolean = false
    override val scaleMode: ScaleMode = ScaleMode.SHOW_ALL

    // todo pass DI to extension with ui/logic separation
    override suspend fun AsyncInjector.configure() {
        mapInstance(
            // fixme
            SoundManager(
                resourcesVfs[MUSIC_WAV]
                    .readMusic()
                    .play(PlaybackTimes.ZERO).apply {
                        volume = 0.02
                    }
            )
        )
        mapPrototype { FirstScene() }
        mapPrototype { SecondScene() }
        mapPrototype { ThirdScene() }
    }
}