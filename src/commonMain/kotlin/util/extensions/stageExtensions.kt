package util.extensions

import com.soywiz.korge.service.storage.InmemoryStorage
import com.soywiz.korge.service.storage.NativeStorage
import com.soywiz.korge.view.Stage
import util.utils.LocalSettingsCache
import util.utils.isPersistentStorage

private var cache: LocalSettingsCache? = null

val Stage.localSettingsCache: LocalSettingsCache
    get() {
        return cache ?: run {
            val storage = if (isPersistentStorage) NativeStorage(views) else InmemoryStorage()
            val newCache = LocalSettingsCache(storage)
            cache = newCache
            newCache
        }
    }