package com.tjx.MeetHere.controller.viewObject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderVO {
    private Long orderId;

    private LocalDateTime orderTime;

    private String username;

    private String venueName;

    private String venueSite;

    private Integer[] selectedSlots;

    private String imgUrl;

    private BigDecimal price;

    private LocalDate reservationDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

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

    public Integer[] getSelectedSlots() {
        return selectedSlots;
    }

    public void setSelectedSlots(Integer[] selectedSlots) {
        this.selectedSlots = selectedSlots;
    }
}
