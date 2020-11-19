package ui.scenes

import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.launchImmediately
import org.jbox2d.dynamics.BodyType
import ui.scenes.base.BaseScene
import util.extensions.addDoor
import util.utils.FOREST_BG
import util.utils.GAME_WINDOW_WIDTH
import util.utils.asGround

class SecondScene : BaseScene() {

    private lateinit var floorBottom: View

    override val backgroundPath = FOREST_BG

    override suspend fun Container.onSceneInitialized() {
        addFloor()
        playerSprite.alignBottomToTopOf(floorBottom).alignX(floorBottom, 0.5, true, 0.0)
        addDoors()
    }

    private suspend fun Container.addFloor() {
        floorBottom = solidRect(GAME_WINDOW_WIDTH, 50, Colors.AQUAMARINE)
            .registerBodyWithFixture(type = BodyType.STATIC)
            .alignBottomToBottomOf(this)
            .asGround()
    }

    private suspend fun Container.addDoors() {
        addDoor(playerSprite = playerSprite, onTouched = {
            launchImmediately {
                sceneContainer.pushTo<FirstScene>()
            }
        }).alignLeftToLeftOf(floorBottom).alignBottomToTopOf(floorBottom)
        addDoor(playerSprite = playerSprite, onTouched = {
            launchImmediately {
                sceneContainer.pushTo<ThirdScene>()
            }
        }).alignRightToRightOf(floorBottom).alignBottomToTopOf(floorBottom)
    }
}
