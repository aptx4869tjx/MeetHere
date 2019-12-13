package com.tjx.MeetHere.controller.viewObject;

import java.time.LocalDate;

public class VenueVO {
    private Long venueId;

    private String venueName;

    private String description;

    private Double price;

    private String site;

    private LocalDate date;//具体日期，与occupiedTimeSlots共同表示，一天中被占用的时段。

    private Byte[] timeSlots;//开放的所有时段,以24小时计数

    private Byte[] occupiedTimeSlots;//占用的时段

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Byte[] getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(Byte[] timeSlots) {
        this.timeSlots = timeSlots;
    }

    public Byte[] getOccupiedTimeSlots() {
        return occupiedTimeSlots;
    }

    public void setOccupiedTimeSlots(Byte[] occupiedTimeSlots) {
        this.occupiedTimeSlots = occupiedTimeSlots;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
