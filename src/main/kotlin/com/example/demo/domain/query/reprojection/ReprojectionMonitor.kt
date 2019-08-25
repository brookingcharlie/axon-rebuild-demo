package com.example.demo.domain.query.reprojection

import org.axonframework.config.EventProcessingConfiguration
import org.axonframework.eventhandling.EventTrackerStatus
import org.axonframework.eventhandling.ReplayToken
import org.axonframework.eventhandling.TrackingEventProcessor
import org.axonframework.eventsourcing.eventstore.GapAwareTrackingToken
import org.axonframework.eventsourcing.eventstore.GlobalSequenceTrackingToken
import org.axonframework.eventsourcing.eventstore.TrackingToken
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.Timer
import kotlin.concurrent.schedule

@Service
class ReprojectionMonitor(
        @Autowired val eventProcessingConfiguration: EventProcessingConfiguration
) {
    final var complete: Boolean = false
        private set

    @Async
    fun run() {
        val eventProcessors = eventProcessingConfiguration.eventProcessors().keys
        val reprojections = eventProcessors.filter { it.startsWith("reprojection-") }
        val reprojectionComplete = reprojections.associate { it to false }.toMutableMap()
        Timer().schedule(0L, 1000L) {
            reprojections
                    .filter { !reprojectionComplete[it]!! }
                    .forEach { reprojectionComplete[it] = checkTracker(it) }
            if (reprojectionComplete.values.all { it }) {
                logger.info("All reprojections complete")
                complete = true
                cancel()
            }
        }
    }

    private fun checkTracker(name: String): Boolean {
        val tracker: TrackingEventProcessor? = eventProcessingConfiguration
                .eventProcessor(name, TrackingEventProcessor::class.java)
                .orElse(null)
        if (tracker == null) {
            logger.debug("No tracker")
            return false
        }
        val statusMap: MutableMap<Int, EventTrackerStatus> = tracker.processingStatus()
        if (statusMap.isEmpty()) {
            logger.debug("No status")
            return false
        }
        val status: EventTrackerStatus = statusMap.values.first()
        printToken(status.trackingToken)
        if (status.isCaughtUp) {
            logger.debug("Caught up")
            tracker.shutDown()
            return true
        }
        return false
    }

    private fun printToken(token: TrackingToken?) {
        when (token) {
            null -> logger.debug("No token")
            is GapAwareTrackingToken -> logger.debug("Up to ${token.index}")
            is GlobalSequenceTrackingToken -> logger.debug("Up to ${token.globalIndex}")
            is ReplayToken -> printToken(token.currentToken)
            else -> logger.debug("Unfamiliar token $token")
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ReprojectionMonitor::class.java)
    }
}