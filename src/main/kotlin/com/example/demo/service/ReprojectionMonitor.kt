package com.example.demo.service

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
import java.util.TimerTask
import kotlin.concurrent.schedule

@Service
class ReprojectionMonitor(
        @Autowired val eventProcessingConfiguration: EventProcessingConfiguration
) {
    private var complete: Boolean = false

    fun isComplete(): Boolean {
        return complete;
    }

    @Async
    fun run() {
        Timer().schedule(0L, 100L) { checkTracker() }
    }

    private fun TimerTask.checkTracker() {
        val tracker: TrackingEventProcessor? = eventProcessingConfiguration
                .eventProcessorByProcessingGroup("reprojection-1", TrackingEventProcessor::class.java)
                .orElse(null)
        if (tracker == null) {
            logger.debug("[monitor] No tracker")
            return
        }
        val statusMap: MutableMap<Int, EventTrackerStatus> = tracker.processingStatus()
        if (statusMap.isEmpty()) {
            logger.debug("[monitor] No status")
            return
        }
        val status: EventTrackerStatus = statusMap.values.first()
        printToken(status.trackingToken)
        if (status.isCaughtUp) {
            logger.debug("[monitor] Caught up")
            tracker.shutDown()
            complete = true;
            cancel()
        }
    }

    private fun printToken(token: TrackingToken?) {
        when (token) {
            null -> logger.debug("[monitor] No token")
            is GapAwareTrackingToken -> logger.debug("[monitor] Up to ${token.index}")
            is GlobalSequenceTrackingToken -> logger.debug("[monitor] Up to ${token.globalIndex}")
            is ReplayToken -> printToken(token.currentToken)
            else -> logger.debug("[monitor] Unfamiliar token $token")
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ReprojectionMonitor::class.java)
    }
}