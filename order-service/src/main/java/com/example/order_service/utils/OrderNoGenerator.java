package com.example.order_service.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OrderNoGenerator {

    private static final String PREFIX = "ORD";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final AtomicInteger SEQUENCE = new AtomicInteger(1000);

    public String generateOrderNo() {
        String dateStr = LocalDateTime.now().format(DATE_FORMATTER);
        int sequence = SEQUENCE.getAndIncrement();
        if (sequence > 9999) {
            SEQUENCE.set(1000);
            sequence = SEQUENCE.getAndIncrement();
        }
        return PREFIX + dateStr + String.format("%04d", sequence);
    }
}
