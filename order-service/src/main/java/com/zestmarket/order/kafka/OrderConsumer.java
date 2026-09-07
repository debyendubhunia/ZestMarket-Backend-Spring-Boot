package com.zestmarket.order.kafka;

import com.zestmarket.order.entity.OrderStatus;
import com.zestmarket.order.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    private final OrderService orderService;

    public OrderConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "payment-events", groupId = "order-group")
    public void consumePaymentEvent(String message) {
        if (message != null && message.startsWith("PAYMENT_SUCCESS:")) {
            String[] parts = message.split(":");
            if (parts.length >= 2) {
                Long orderId = Long.parseLong(parts[1]);
                orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED);
            }
        }
    }
}
