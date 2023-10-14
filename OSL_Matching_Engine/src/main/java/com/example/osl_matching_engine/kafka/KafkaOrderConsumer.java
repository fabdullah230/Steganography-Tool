package com.example.osl_matching_engine.kafka;

import com.example.osl_matching_engine.engine.OrderMatchingEngine;
import com.example.osl_matching_engine.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaOrderConsumer {

    private final OrderMatchingEngine orderMatchingEngine;

    @Autowired
    public KafkaOrderConsumer(OrderMatchingEngine orderMatchingEngine) {
        this.orderMatchingEngine = orderMatchingEngine;
    }

    @KafkaListener(topics = "orders", groupId = "order_group", containerFactory = "orderKafkaListenerContainerFactory")
    public void consume(Order order) {
        orderMatchingEngine.processOrder(order);
    }
}
