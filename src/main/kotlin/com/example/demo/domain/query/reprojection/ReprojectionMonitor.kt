package com.example.demo.domain.query.reprojection

import org.axonframework.config.EventProcessingConfiguration
import org.axonframework.eventhandling.EventTrackerStatus
import org.axonframework.eventhandling.TrackingEventProcessor
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
            logger.info("Tracker $name not found")
            return false
        }
        val statusMap: MutableMap<Int, EventTrackerStatus> = tracker.processingStatus()
        if (statusMap.isEmpty()) {
            logger.info("Tracker $name has no active segments")
            return false
        }
        val status: EventTrackerStatus = statusMap.values.first()
        logger.info("Tracker $name token ${status.trackingToken}")
        if (status.isCaughtUp) {
            logger.info("Tracker $name has caught up")
            tracker.shutDown()
            return true
        }
        return false
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ReprojectionMonitor::class.java)
    }
}