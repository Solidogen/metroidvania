package ui.scenes.base

import com.soywiz.korge.box2d.body
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import util.extensions.addBackground
import util.extensions.addBasicHud
import util.extensions.localSettingsCache
import util.utils.*

abstract class BaseScene : Scene() {

    /**
     * todo move those to DI after cleaning Scenes will be fixed in https://github.com/korlibs/korge/issues/144
     * */
    private var playerMovementManager: PlayerMovementManager? = null
    private var playerAnimationManager: PlayerAnimationManager? = null
    private val localSettingsCache: LocalSettingsCache by lazy {
        sceneContainer.stage?.localSettingsCache ?: error("Stage not set, ")
    }
    private var container: Container? = null
    private var _playerSprite: Sprite? = null

    var playerStatisticsManager: PlayerStatisticsManager? = null
        private set

    val playerSprite
        get() = _playerSprite!!
    val playerBody
        get() = _playerSprite!!.body!!

    abstract val backgroundPath: String

    // todo refactor
    final override suspend fun Container.sceneInit() {
        println("SCENE INIT")
        container = this

        val animationManager = PlayerAnimationManager()
        animationManager.init()
        playerAnimationManager = animationManager
        playerMovementManager = PlayerMovementManager(animationManager)
        playerStatisticsManager = PlayerStatisticsManager()

        addBackground(backgroundPath)

        val sprite = sprite(animationManager.idleAnimation) {
            name = "Player"
        }
        _playerSprite = sprite
        animationManager.playerSprite = sprite

        addBasicHud(scene = this@BaseScene, localSettingsCache = localSettingsCache)
        addMovement()
        onSceneInitialized()
    }

    final override suspend fun sceneBeforeLeaving() {
        println("SCENE LEAVING")
        playerMovementManager = null
        playerAnimationManager = null
        playerStatisticsManager = null
        container?.removeChildren()
        _playerSprite = null
        container = null
        onSceneLeaving()
    }

    abstract suspend fun Container.onSceneInitialized()

    open suspend fun onSceneLeaving() = Unit

    private suspend fun Container.addMovement() {
        playerMovementManager?.addMovement(
            playerSprite = playerSprite,
            keys = this@BaseScene.views.keys
        )
    }
}