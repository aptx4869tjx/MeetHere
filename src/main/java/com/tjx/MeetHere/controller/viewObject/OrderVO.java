package com.tjx.MeetHere.controller.viewObject;

import java.time.LocalDateTime;

public class OrderVO {
    private Long orderId;


    private LocalDateTime orderTime;

    private String venueName;

    private String venueSite;

    private Byte[] selectedSlots;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }



    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueSite() {
        return venueSite;
    }

    public void setVenueSite(String venueSite) {
        this.venueSite = venueSite;
    }

    public Byte[] getSelectedSlots() {
        return selectedSlots;
    }

    public void setSelectedSlots(Byte[] selectedSlots) {
        this.selectedSlots = selectedSlots;
    }
}
