package util.utils

import com.soywiz.korge.service.storage.IStorage
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class LocalSettingsCache(
    private val storage: IStorage
) {

    var experience: Int by IntStorageProperty(key = EXPERIENCE_KEY, defaultValue = 0)
    var exampleBool: Boolean by BooleanStorageProperty(key = EXAMPLE_BOOL_KEY, defaultValue = false)

    private inner class IntStorageProperty(private val key: String, private val defaultValue: Int) : ReadWriteProperty<Any, Int> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Int {
            return storage.getOrNull(key)?.toIntOrNull() ?: defaultValue
        }
        override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
            storage[key] = value.toString()
        }
    }

    private inner class BooleanStorageProperty(private val key: String, private val defaultValue: Boolean) : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return storage.getOrNull(key)?.toBoolean() ?: defaultValue
        }
        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            storage[key] = value.toString()
        }
    }

    companion object {
        private const val EXPERIENCE_KEY = "EK_20934587"
        private const val EXAMPLE_BOOL_KEY = "EBK_34572350"
    }
}