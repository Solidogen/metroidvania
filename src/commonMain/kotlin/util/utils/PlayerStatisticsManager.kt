package util.utils

import kotlinx.coroutines.delay

// todo stats etc
// todo singleton with local caching
class PlayerStatisticsManager {

    var canReceiveHit = true

    var health = 500

    suspend fun receiveHit() {
        if (canReceiveHit) {
            println("Hit received")
            canReceiveHit = false
            val healthLost = (1..40).random()
            health -= healthLost
            delay(500)
            canReceiveHit = true
        }
    }
}