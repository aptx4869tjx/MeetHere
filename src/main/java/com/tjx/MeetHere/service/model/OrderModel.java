package com.tjx.MeetHere.service.model;

import java.time.LocalDateTime;

public class OrderModel {
    private Long orderId;

    private Long userId;

    private LocalDateTime orderTime;

    private Long venueId;

    private int[] occupiedTimeSlots;

    private Byte isChecked;
}
