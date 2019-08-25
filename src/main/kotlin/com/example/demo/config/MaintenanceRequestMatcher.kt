package com.example.demo.config

import com.example.demo.domain.query.reprojection.ReprojectionMonitor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class MaintenanceRequestMatcher : RequestMatcher {
    @Autowired
    lateinit var reprojectionMonitor: ReprojectionMonitor

    override fun matches(request: HttpServletRequest): Boolean {
        return !reprojectionMonitor.isComplete()
    }
}
