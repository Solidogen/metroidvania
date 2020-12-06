package util.utils

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.centerOn
import com.soywiz.korge.view.text
import kotlinx.coroutines.delay

class PlayerStatisticsManager {

    var canReceiveHit = true

    var health = 500

    suspend fun receiveHit(container: Container) {
        if (canReceiveHit) {
            println("Hit received")
            canReceiveHit = false
            val healthLost = (1..40).random()
            health -= healthLost
            val damageText = container.text("Health lost: $healthLost. Remaining health: $health") {
                name = "Damage text"
            }.centerOn(container)
            delay(1000)
            container.removeChild(damageText)
            canReceiveHit = true
        }
    }
}