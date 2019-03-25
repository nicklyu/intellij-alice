package org.intellij.alice.server.data

object Commands {
    val commands = mapOf(
            "запустить проект" to "Run",
            "создать проект" to "NewProject",
            "перезапустить проект" to "Rerun",
            "подтянуть последние изменения" to "Git.Fetch",
            "выйти" to "Exit",
            "проверить код" to "InspectCode",
            "остановить проект" to "Stop",
            "собрать проект" to "Compile"
    )
}