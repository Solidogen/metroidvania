package util.utils

import com.soywiz.korge.box2d.body
import com.soywiz.korge.view.View
import org.jbox2d.dynamics.Body
import org.jbox2d.userdata.Box2dTypedUserData

object IsGround : Box2dTypedUserData.Key<Boolean>()

var Body.isGround: Boolean
    get() = get(IsGround) ?: false
    set(value) { set(IsGround, value) }

fun <T : View> T.asGround(): T {
    body?.isGround = true
    return this
}