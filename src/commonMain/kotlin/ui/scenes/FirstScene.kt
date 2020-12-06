package ui.scenes

import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.launchImmediately
import org.jbox2d.dynamics.BodyType
import ui.scenes.base.BaseScene
import util.extensions.addDoor
import util.extensions.addSkeleton
import util.utils.GAME_WINDOW_WIDTH
import util.utils.SUNSET_BG
import util.utils.asGround

class FirstScene : BaseScene() {

    private lateinit var floorBottom: View

    override val backgroundPath = SUNSET_BG

    override suspend fun Container.onSceneInitialized() {
        addFloor()
        playerSprite.alignBottomToTopOf(floorBottom).alignX(floorBottom, 0.5, true, 0.0)
        addDoors()
        addEnemies()
    }

    private suspend fun Container.addFloor() {
        floorBottom = solidRect(GAME_WINDOW_WIDTH, 50, Colors.ORANGE)
            .registerBodyWithFixture(type = BodyType.STATIC)
            .alignBottomToBottomOf(this)
            .asGround()
        floorBottom.name = "Floor"
    }

    private suspend fun Container.addDoors() {
        addDoor(playerSprite = playerSprite, onTouched = {
            launchImmediately {
                sceneContainer.pushTo<SecondScene>()
            }
        }).alignRightToRightOf(floorBottom).alignBottomToTopOf(floorBottom)
    }

    private suspend fun Container.addEnemies() {
        addSkeleton(playerSprite = playerSprite, onTouched = {
            launchImmediately {
                playerStatisticsManager?.receiveHit()
            }
        }).alignBottomToTopOf(floorBottom).alignX(floorBottom, 0.2, true, 0.0)
    }
}
