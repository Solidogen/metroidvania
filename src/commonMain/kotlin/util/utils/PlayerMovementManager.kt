package util.utils

import com.soywiz.klock.timesPerSecond
import com.soywiz.korev.Key
import com.soywiz.korge.box2d.body
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.input.InputKeys
import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.addFixedUpdater
import com.soywiz.korge.view.addUpdater
import org.jbox2d.callbacks.ContactImpulse
import org.jbox2d.callbacks.ContactListener
import org.jbox2d.collision.Manifold
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.contacts.Contact
import org.jbox2d.dynamics.contacts.ContactEdge
import util.extensions.isIdle
import util.extensions.isTouchingGround
import util.utils.PlayerAnimationManager.PlayerAnimation

class PlayerMovementManager(
    private val playerAnimationManager: PlayerAnimationManager
) {
    private var horizontalMovementState = HorizontalMovementState.None
    private var jumpState = JumpState.None
    private var attackState = AttackState.None

    suspend fun addMovement(playerSprite: Sprite, keys: InputKeys) {
        playerSprite.anchorX = 0.5
        playerSprite.registerBodyWithFixture(
            type = BodyType.DYNAMIC,
            restitution = 0,
            fixedRotation = true,
            density = 1
        )
        // todo add bottom ground sensor here

        /**
         * todo not sure how it interacts with isTouchingGround. CHECK [AABB] (seems unreachable by user code for some reason)
         * */
        playerSprite.body?.world?.setContactListener(object : ContactListener {
            override fun preSolve(contact: Contact, oldManifold: Manifold) {
                contact.m_restitution = 0f
            }

            override fun beginContact(contact: Contact) {
            }

            override fun endContact(contact: Contact) {
            }

            override fun postSolve(contact: Contact, impulse: ContactImpulse) {
            }
        })

        playerSprite.addUpdater {
            horizontalMovementState = HorizontalMovementState.None
            if (keys[Key.LEFT]) {
                horizontalMovementState = HorizontalMovementState.Left
            } else if (keys[Key.RIGHT]) {
                horizontalMovementState = HorizontalMovementState.Right
            }
            if (keys.justPressed(Key.UP) && playerSprite.isTouchingGround) {
                if (jumpState == JumpState.None) {
                    jumpState = JumpState.NeedsToJump
                }
            } else if (keys.justPressed(Key.DOWN)) {
                /* isTouchingDescendablePlatform(playerSprite)*/
                // todo drop down from one-way platform OR crouch
            }
            if (keys.justPressed(Key.SPACE)) {
                if (attackState == AttackState.None) {
                    attackState = AttackState.NeedsToAttack
                }
            }
        }

        // todo this is pretty much unmaintainable, need state machine
        playerSprite.addFixedUpdater(60.timesPerSecond) {
            if (playerAnimationManager.currentAnimation == PlayerAnimation.Attack && playerSprite.isTouchingGround) {
                playerSprite.body?.linearVelocityX = 0f
            } else {
                playerSprite.body?.linearVelocityX = horizontalMovementState.rawState * BASE_MOVEMENT_SPEED
            }
            when (horizontalMovementState) {
                HorizontalMovementState.Left -> playerSprite.scaleX = -1.0
                HorizontalMovementState.Right -> playerSprite.scaleX = 1.0
                HorizontalMovementState.None -> Unit
            }
            if (horizontalMovementState != HorizontalMovementState.None
                && playerSprite.isTouchingGround
                && jumpState != JumpState.IsJumping
                && attackState != AttackState.IsAttacking
            ) {
                playerAnimationManager.playRunAnimation()
            }
            if (playerSprite.body?.linearVelocityY == 0f && playerSprite.isTouchingGround && jumpState == JumpState.IsJumping) {
                jumpState = JumpState.None
            }
            if (playerSprite.body?.isIdle == true && playerAnimationManager.currentAnimation != PlayerAnimation.Attack) {
                playerAnimationManager.playIdleAnimation()
            }
            if (jumpState == JumpState.NeedsToJump) {
                playerSprite.body?.applyForceToCenter(Vec2(0f, -BASE_JUMPING_FORCE))
                jumpState = JumpState.IsJumping
            }
            if (jumpState == JumpState.IsJumping && attackState != AttackState.IsAttacking) {
                playerAnimationManager.playJumpAnimation()
            }
            if (attackState == AttackState.NeedsToAttack) {
                playerAnimationManager.playAttackAnimation()
                attackState = AttackState.IsAttacking
            }
            if (playerAnimationManager.currentAnimation != PlayerAnimation.Attack) {
                attackState = AttackState.None
            }
//            println("Jump: $jumpState, attack: $attackState, currentAnimation: ${playerAnimationManager.currentAnimation}")
        }
    }

    enum class HorizontalMovementState(val rawState: Int) {
        Left(-1),
        Right(1),
        None(0)
    }

    enum class JumpState {
        NeedsToJump,
        IsJumping,
        None
    }

    enum class AttackState {
        NeedsToAttack,
        IsAttacking,
        None
    }

    companion object {
        const val BASE_MOVEMENT_SPEED = 10f
        const val BASE_JUMPING_FORCE = 10000f
        const val GRAVITY = 10f
    }
}