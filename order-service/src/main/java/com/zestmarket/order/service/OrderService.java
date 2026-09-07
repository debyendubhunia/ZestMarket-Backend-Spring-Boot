package com.zestmarket.order.service;

import com.zestmarket.order.dto.*;
import com.zestmarket.order.entity.*;
import com.zestmarket.order.repository.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public interface OrderService {
    OrderDTO createOrder(Long userId, CreateOrderRequest request);
    OrderDTO getOrderById(Long id);
    List<OrderDTO> getOrdersByUserId(Long userId);
    OrderDTO updateOrderStatus(Long orderId, OrderStatus status);
    DashboardStatsDTO getDashboardStats();
}

@Service
@Transactional
class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderServiceImpl(OrderRepository orderRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public OrderDTO createOrder(Long userId, CreateOrderRequest request) {
        Order order = new Order();
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setUserId(userId);
        order.setShippingAddress(request.getShippingAddress());
        order.setOrderStatus(OrderStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequest itemReq : request.getItems()) {
            BigDecimal price = itemReq.getPrice() != null ? itemReq.getPrice() : BigDecimal.ZERO;
            int qty = itemReq.getQuantity() > 0 ? itemReq.getQuantity() : 1;
            BigDecimal lineTotal = price.multiply(new BigDecimal(qty));
            total = total.add(lineTotal);
            order.getItems().add(new OrderItem(order, itemReq.getProductId(), qty, price));
        }
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        // Publish Order Created Event to Kafka
        try {
            kafkaTemplate.send("order-events", "ORDER_CREATED:" + saved.getId() + ":" + saved.getOrderNumber());
        } catch (Exception e) {
            System.err.println("Kafka Producer Exception: " + e.getMessage());
        }

        return mapToDTO(saved);
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return mapToDTO(order);
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.setOrderStatus(status);
        return mapToDTO(orderRepository.save(order));
    }

    @Override
    public DashboardStatsDTO getDashboardStats() {
        Double revenue = orderRepository.calculateTotalRevenue();
        Long recent = orderRepository.countOrdersSince(LocalDateTime.now().minusDays(1));
        Long pending = (long) orderRepository.findByOrderStatusAndCreatedAtBefore(OrderStatus.PENDING, LocalDateTime.now()).size();
        Long confirmed = (long) orderRepository.findByOrderStatusAndCreatedAtBefore(OrderStatus.CONFIRMED, LocalDateTime.now()).size();

        return new DashboardStatsDTO(
                revenue != null ? revenue : 0.0,
                recent != null ? recent : 0L,
                pending,
                confirmed
        );
    }

    // Auto Cancellation Scheduler for unpaid orders older than 15 minutes
    @Scheduled(cron = "0 */5 * * * *")
    public void cancelUnpaidOrders() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(15);
        List<Order> unpaidOrders = orderRepository.findByOrderStatusAndCreatedAtBefore(OrderStatus.PENDING, cutoff);

        for (Order order : unpaidOrders) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            try {
                kafkaTemplate.send("order-events", "ORDER_CANCELLED:" + order.getId());
            } catch (Exception e) {
                System.err.println("Kafka Producer Error on Order Cancellation: " + e.getMessage());
            }
        }
    }

    private OrderDTO mapToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setUserId(order.getUserId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setItems(order.getItems().stream()
                .map(i -> new OrderItemRequest(i.getProductId(), i.getQuantity(), i.getPrice()))
                .collect(Collectors.toList()));
        return dto;
    }
}
