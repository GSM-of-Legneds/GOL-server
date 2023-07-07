package dev.yangsijun.gol.common.common.util

import java.lang.reflect.Modifier
import java.util.*

class GolObjectUtils {
    companion object {
        fun reflectionToString(obj: Any): String {
            val s = LinkedList<String>()
            var clazz: Class<in Any>? = obj.javaClass
            while (clazz != null) {
                for (prop in clazz.declaredFields.filterNot { Modifier.isStatic(it.modifiers) }) {
                    prop.isAccessible = true
                    s += "${prop.name}=" + prop.get(obj)?.toString()?.trim()
                }
                clazz = clazz.superclass
            }
            return "${obj.javaClass.simpleName}=[${s.joinToString(", ")}]"
        }
    }
}
