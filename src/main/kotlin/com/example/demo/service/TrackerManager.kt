package com.example.demo.service

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
class TrackerManager(
        @Autowired val eventProcessingConfiguration: EventProcessingConfiguration,
        @Autowired val globalIndexService: GlobalIndexService
) {
    fun shutDown() {
        logger.debug("shutDown()")
        getTracker().shutDown()
    }

    fun start() {
        logger.debug("start()")
        getTracker().start()
        logWhenCaughtUp()
    }

    @Async
    fun logWhenCaughtUp() {
        val tracker = getTracker()
        val startTime = System.currentTimeMillis()
        Timer().schedule(0L, 100L) {
            val status = tracker.processingStatus()
            if (status.isNotEmpty() && status.values.all { it.trackingToken != null && it.isCaughtUp }) {
                val endTime = System.currentTimeMillis()
                logger.debug("Caught up in < ${endTime - startTime} ms")
                cancel()
            }
        }
    }

    fun rebuild() {
        logger.debug("rebuild()")
        getTracker().apply { shutDown(); resetTokens(); start() }
        logWhenCaughtUp()
    }

    fun status(): Status {
        logger.debug("status()")
        val segmentStatusesMap: Map<Int, EventTrackerStatus> = getTracker().processingStatus()
        return Status(
                segmentStatuses = segmentStatusesMap.values.toList(),
                globalIndex = globalIndexService.get()
        )
    }

    data class Status(
            val segmentStatuses: List<EventTrackerStatus>,
            val globalIndex: Long
    )

    private fun getTracker(): TrackingEventProcessor {
        return eventProcessingConfiguration
                .eventProcessorByProcessingGroup("reprojection-1", TrackingEventProcessor::class.java)
                .get()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(TrackerManager::class.java)
    }
}