package com.example.osl_matching_engine.engine;

import com.example.osl_matching_engine.kafka.KafkaOrderProducer;
import com.example.osl_matching_engine.models.Order;
import com.example.osl_matching_engine.models.OrderBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderMatchingEngine {
    private final OrderBook orderBook = new OrderBook();
    private final KafkaOrderProducer kafkaOrderProducer;

    @Autowired
    public OrderMatchingEngine(KafkaOrderProducer kafkaOrderProducer) {
        this.kafkaOrderProducer = kafkaOrderProducer;
    }

    public void processOrder(Order order) {
        Optional<Order> matchedOrder = orderBook.matchOrder(order);
        if (matchedOrder.isPresent()) {
            // Execute the trade
            executeTrade(order, matchedOrder.get());
        } else {
            // Add the order to the order book
            orderBook.addOrder(order);
            kafkaOrderProducer.sendMessage("Order added to book: " + order);
        }
    }

    private void executeTrade(Order order1, Order order2) {
        int tradeQuantity = Math.min(order1.getQuantity(), order2.getQuantity());

        // Adjust the quantities of the orders
        order1.setQuantity(order1.getQuantity() - tradeQuantity);
        order2.setQuantity(order2.getQuantity() - tradeQuantity);

        // If there is any remaining quantity, add the orders back to the order book
        if (order1.getQuantity() > 0) {
            orderBook.addOrder(order1);
        }
        if (order2.getQuantity() > 0) {
            orderBook.addOrder(order2);
        }

        kafkaOrderProducer.sendMessage("Trade executed: " + tradeQuantity + " @ " + order1.getPrice());
    }
}