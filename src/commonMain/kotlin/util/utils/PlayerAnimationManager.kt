package util.utils

import com.soywiz.klock.milliseconds
import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.SpriteAnimation
import com.soywiz.korge.view.getSpriteAnimation
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korio.file.std.resourcesVfs

class PlayerAnimationManager {

    private lateinit var runAnimation: SpriteAnimation
    private lateinit var jumpAnimation: SpriteAnimation
    private lateinit var attackAnimation: SpriteAnimation

    lateinit var playerSprite: Sprite
    lateinit var idleAnimation: SpriteAnimation
        private set

    var currentAnimation: PlayerAnimation = PlayerAnimation.Idle
        private set

    suspend fun init() {
        val atlas = resourcesVfs[ATLAS_JSON].readAtlas()
        runAnimation = atlas.getSpriteAnimation(HERO_RUN_ANIM_PREFIX)
        jumpAnimation = atlas.getSpriteAnimation(HERO_JUMP_ANIM_PREFIX)
        idleAnimation = atlas.getSpriteAnimation(HERO_IDLE_ANIM_PREFIX)
        attackAnimation = atlas.getSpriteAnimation(HERO_ATTACK_ANIM_PREFIX)
    }

    fun playRunAnimation() {
        currentAnimation = PlayerAnimation.Run
        playerSprite.playAnimationLooped(runAnimation, spriteDisplayTime = animationTime)
    }

    fun playJumpAnimation() {
        currentAnimation = PlayerAnimation.Jump
        playerSprite.playAnimation(jumpAnimation, spriteDisplayTime = animationTime)
    }

    fun playIdleAnimation() {
        currentAnimation = PlayerAnimation.Idle
        playerSprite.playAnimationLooped(idleAnimation, spriteDisplayTime = animationTime)
    }

    fun playAttackAnimation() {
        currentAnimation = PlayerAnimation.Attack
        playerSprite.playAnimation(attackAnimation, spriteDisplayTime = attackAnimationTime)
        playerSprite.onAnimationCompleted.once {
            currentAnimation = PlayerAnimation.Unknown
        }
    }

    enum class PlayerAnimation {
        Run,
        Jump,
        Idle,
        Attack,
        Unknown
    }

    companion object {
        private val animationTime = 200.milliseconds
        private val attackAnimationTime = 100.milliseconds
    }
}