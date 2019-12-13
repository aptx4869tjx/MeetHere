package com.tjx.MeetHere.service.model;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderModel {
    private Long orderId;

    @NotNull(message = "用户不能为空")
    private Long userId;

    private LocalDateTime orderTime;

    @NotNull(message = "场地号不能为空")
    private Long venueId;

    @NotNull(message = "预约时间不能为空")
    private Byte[] occupiedTimeSlots;

    private Byte isChecked;

    @NotNull(message = "预约日期不能为空")
    private LocalDate date;//预约日期

    private BigDecimal price;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public Byte[] getOccupiedTimeSlots() {
        return occupiedTimeSlots;
    }

    public void setOccupiedTimeSlots(Byte[] occupiedTimeSlots) {
        this.occupiedTimeSlots = occupiedTimeSlots;
    }

    public Byte getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Byte isChecked) {
        this.isChecked = isChecked;
    }
}
