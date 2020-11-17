package util.extensions

import com.soywiz.korge.box2d.body
import com.soywiz.korge.box2d.view
import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.View
import org.jbox2d.dynamics.contacts.ContactEdge
import util.utils.isGround

// todo check if body fully over obstacle, if yes, set Y axis to other body top
//  use scanner or something
val Sprite.isTouchingGround: Boolean
    get() {
        var ce: ContactEdge? = body?.getContactList()
        while (ce != null) {
            if (ce.other?.isGround == true && ce.contact?.isTouching == true) {
//                println("THIS POS: $globalBounds, VIEW POS ${ce.other?.view?.globalBounds}")
                return true
            }
            ce = ce.next
        }
        return false
    }

fun Sprite.isTouching(view: View): Boolean {
    var ce: ContactEdge? = body?.getContactList()
    while (ce != null) {
        // todo contact listener?
        if (ce.other?.view == view && ce.contact?.isTouching == true) {
//            println("THIS POS: $globalBounds, VIEW POS ${ce.other?.view?.globalBounds}")
            return true
        }
        ce = ce.next
    }
    return false
}