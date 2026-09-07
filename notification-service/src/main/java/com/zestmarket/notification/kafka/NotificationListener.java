package com.zestmarket.notification.kafka;

import com.zestmarket.notification.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private final EmailService emailService;

    public NotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void consumeOrderEvent(String message) {
        System.out.println("Received Order Event: " + message);
        if (message.startsWith("ORDER_CREATED:")) {
            emailService.sendNotificationEmail(
                    "customer@example.com",
                    "zestmarket: Order Received!",
                    "Your order " + message + " has been placed and is currently PENDING payment."
            );
        } else if (message.startsWith("ORDER_CANCELLED:")) {
            emailService.sendNotificationEmail(
                    "customer@example.com",
                    "zestmarket: Order Cancelled",
                    "Your order " + message + " has been cancelled due to payment timeout."
            );
        }
    }

    @KafkaListener(topics = "payment-events", groupId = "notification-group")
    public void consumePaymentEvent(String message) {
        System.out.println("Received Payment Event: " + message);
        if (message.startsWith("PAYMENT_SUCCESS:")) {
            emailService.sendNotificationEmail(
                    "customer@example.com",
                    "zestmarket: Payment Confirmed!",
                    "Your payment for " + message + " was successful. Your order is now CONFIRMED!"
            );
        }
    }
}
