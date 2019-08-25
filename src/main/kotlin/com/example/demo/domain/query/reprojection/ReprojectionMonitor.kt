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
    private var complete: Boolean = false

    fun isComplete(): Boolean {
        return complete;
    }

    @Async
    fun run() {
        val processingGroups: List<String> = listOf()
        //val processingGroups: List<String> = listOf("reprojection-1")
        //val processingGroups: List<String> = listOf("reprojection-1", "reprojection-2")
        val processingGroupComplete = processingGroups.associate { it to false }.toMutableMap()
        Timer().schedule(0L, 100L) {
            processingGroups
                    .filter { !processingGroupComplete[it]!! }
                    .forEach { processingGroup ->
                        if (checkTracker(processingGroup)) {
                            logger.info("Reprojection $processingGroup complete")
                            processingGroupComplete[processingGroup] = true
                        }
                    }
            if (processingGroupComplete.values.all { it }) {
                logger.info("All reprojections complete")
                complete = true
                cancel()
            }
        }
    }

    private fun checkTracker(processingGroup: String): Boolean {
        val tracker: TrackingEventProcessor? = eventProcessingConfiguration
                .eventProcessorByProcessingGroup(processingGroup, TrackingEventProcessor::class.java)
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