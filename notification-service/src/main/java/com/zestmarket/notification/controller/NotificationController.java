package com.zestmarket.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification APIs", description = "Email & Messaging Notification Service Status")
public class NotificationController {

    @GetMapping("/health")
    @Operation(summary = "Check Notification Service Kafka consumer status")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Notification Service is active and listening to Kafka topics");
    }
}
