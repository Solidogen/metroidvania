package util.extensions

import com.soywiz.klock.timesPerSecond
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.delay
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import org.jbox2d.dynamics.BodyType
import util.utils.*
import kotlin.math.round

suspend fun Container.addBackground(path: String) {
    image(resourcesVfs[path].readBitmap()).apply {
        name = "Background"
    }
}

suspend fun Container.addBasicHud(scene: Scene, localSettingsCache: LocalSettingsCache) {
    val expBackground = solidRect(200, 30) {
        name = "Exp Background"
    }.alignTopToTopOf(this, 10).alignLeftToLeftOf(this, 10)
    val experience = text(text = "") {
        textSize = 20.0
        color = Colors.BLUEVIOLET
        name = "Experience"
    }.alignTopToTopOf(expBackground, 8).alignLeftToLeftOf(expBackground, 8)

    val controlsContainer = container {
        name = "Controls Container"
    }
    val controlsBackground = controlsContainer.solidRect(200, 80)
        .alignTopToTopOf(this, 10).alignRightToRightOf(this, 10).apply {
            name = "Controls Background"
        }
    val controls = controlsContainer.text(text = "UP - Jump\nLEFT/RIGHT - Move\nSpace - Attack") {
        textSize = 20.0
        color = Colors.BLUEVIOLET
        name = "Controls"
    }.centerOn(controlsBackground)

    experience.addFixedUpdater(10.timesPerSecond) {
        scene.launch {
            text = "EXPERIENCE: ${localSettingsCache.experience}"
            localSettingsCache.experience++
        }
    }
}

suspend fun Container.addDoor(playerSprite: Sprite, onTouched: () -> Unit): Sprite {
    return sprite(resourcesVfs[DOOR_PNG].readBitmap()) {
        name = "Door"
    }.registerBodyWithFixture(type = BodyType.STATIC).apply {
        var doorTouched = false
        addUpdater {
            if (playerSprite.isTouching(this)) {
                if (!doorTouched) {
                    doorTouched = true
                    println("TOUCHING DOOR")
                    onTouched()
                    this@addDoor.stage?.launchImmediately {
                        kotlinx.coroutines.delay(1000)
                        doorTouched = false
                    }
                }
            }
        }
    }
}

suspend fun Container.addSkeleton(playerSprite: Sprite, onTouched: () -> Unit): Sprite {
    val atlas = resourcesVfs[ATLAS_JSON].readAtlas()
    val skeletonAnimation = atlas.getSpriteAnimation(SKELETON_MOVE_ANIM_PREFIX)
    return sprite(skeletonAnimation) {
        name = "Skeleton"
    }.registerBodyWithFixture(type = BodyType.KINEMATIC).apply {
        addUpdater {
            if (playerSprite.isTouching(this)) {
                onTouched()
            }
        }
    }
}