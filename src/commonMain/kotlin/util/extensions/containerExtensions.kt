package util.extensions

import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launch
import com.soywiz.korio.file.std.resourcesVfs
import org.jbox2d.dynamics.BodyType
import util.utils.*
import kotlin.math.round

suspend fun Container.addBackground(path: String) {
    image(resourcesVfs[path].readBitmap())
}

suspend fun Container.addBasicHud(scene: Scene, localSettingsCache: LocalSettingsCache) {
    val experience = text(text = "") {
        textSize = 20.0
        color = Colors.BLUEVIOLET
    }.alignTopToTopOf(this, 20).alignLeftToLeftOf(this, 20)
    experience.addUpdater {
        scene.launch {
            text = "EXPERIENCE: ${localSettingsCache.experience}"
            localSettingsCache.experience++
        }
    }
}

suspend fun Container.addDoor(playerSprite: Sprite, onTouched: () -> Unit): Sprite {
    return sprite(resourcesVfs[DOOR_PNG].readBitmap())
        .registerBodyWithFixture(type = BodyType.STATIC)
        .apply {
            addUpdater {
                if (playerSprite.isTouching(this)) {
                    onTouched()
                }
            }
        }
}

suspend fun Container.addSkeleton(playerSprite: Sprite, onTouched: () -> Unit): Sprite {
    val atlas = resourcesVfs[ATLAS_JSON].readAtlas()
    val skeletonAnimation = atlas.getSpriteAnimation(SKELETON_MOVE_ANIM_PREFIX)
    return sprite(skeletonAnimation)
        .registerBodyWithFixture(type = BodyType.KINEMATIC)
        .apply {
            addUpdater {
                if (playerSprite.isTouching(this)) {
                    onTouched()
                }
            }
        }
}

suspend fun Container.debugMouse(views: Views) {
    val mouseNativeCoords = text("Native: 0, 0").xy(400, 400).apply {
        addUpdater {
            text = "Native: ${round(views.nativeMouseX)}, ${round(views.nativeMouseY)}"
        }
    }
    val mouseLocalCoords = text("Local: 0, 0").xy(400, 500).apply {
        addUpdater {
            text = "Local: ${round(localMouseX(views))}, ${round(localMouseY(views))}"
        }
    }
}