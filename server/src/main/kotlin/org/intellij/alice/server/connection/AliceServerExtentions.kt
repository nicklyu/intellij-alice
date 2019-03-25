package org.intellij.alice.server.connection

internal fun String?.processName(): String? {

    val result = StringBuilder()
    this?.forEach {
        val temp = it.plus(975)
        if (temp in 'а'..'я' || temp in 'А'..'Я')
            result.append(temp)
        else
            result.append(it)
    } ?: return null
    return result.toString().toLowerCase()
}