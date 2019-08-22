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
import java.util.Optional
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.schedule

@Service
class TrackerMonitor(
        @Autowired val eventProcessingConfiguration: EventProcessingConfiguration
) {
    @Async
    fun run() {
        Timer().schedule(0L, 100L) { checkTracker() }
    }

    private fun TimerTask.checkTracker() {
        val tracker: Optional<TrackingEventProcessor> = getTracker()
        if (tracker.isEmpty) {
            logger.debug("[monitor] No tracker")
            return
        }
        val statusMap: MutableMap<Int, EventTrackerStatus> = tracker.get().processingStatus()
        if (statusMap.isEmpty()) {
            logger.debug("[monitor] No status")
            return
        }
        val status: EventTrackerStatus = statusMap.values.first()
        printToken(status.trackingToken)
        if (status.isCaughtUp) {
            logger.debug("[monitor] Caught up")
            tracker.get().shutDown()
            cancel()
        }
    }

    private fun printToken(token: TrackingToken?) {
        when (token) {
            is GapAwareTrackingToken -> logger.debug("[monitor] Up to ${token.index}")
            is GlobalSequenceTrackingToken -> logger.debug("[monitor] Up to ${token.globalIndex}")
            is ReplayToken -> printToken(token.currentToken)
            null -> logger.debug("[monitor] No token")
            else -> logger.debug("[monitor] Unfamiliar token $token")
        }
    }

    private fun getTracker(): Optional<TrackingEventProcessor> {
        return eventProcessingConfiguration
                .eventProcessorByProcessingGroup("tracker", TrackingEventProcessor::class.java)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(TrackerMonitor::class.java)
    }
}