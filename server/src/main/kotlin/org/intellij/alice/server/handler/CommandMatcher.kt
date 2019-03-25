package org.intellij.alice.server.handler

import org.intellij.alice.server.data.Commands
import org.simmetrics.metrics.StringMetrics

object CommandMatcher {
    private const val THRESHOLD = 0.55

    fun findCommand(phrase: String): Pair<String, Float>? {
        val metric = StringMetrics.qGramsDistance()
        Commands.commands.keys
                .map { it to metric.compare(it, phrase) }
                .filter { it.second > THRESHOLD }
                .sortedBy { it.second }
                .firstOrNull()?.let {
                    return Pair(Commands.commands.getValue(it.first), it.second)
                } ?: return null
    }
}