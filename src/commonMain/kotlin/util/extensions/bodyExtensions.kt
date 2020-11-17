package util.extensions

import org.jbox2d.dynamics.Body

val Body.isIdle: Boolean
    get() = linearVelocityX == 0f && linearVelocityY == 0f