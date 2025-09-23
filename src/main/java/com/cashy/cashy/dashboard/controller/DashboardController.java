package com.cashy.cashy.dashboard.controller;

import com.cashy.cashy.dashboard.dto.DashboardDTO;
import com.cashy.cashy.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users/{userId}/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboardData(
            @PathVariable UUID userId,
            Authentication authentication
            ) {
        String email = authentication.getName();

        return  ResponseEntity.ok(dashboardService.getDashboardData(userId));

    }
}
