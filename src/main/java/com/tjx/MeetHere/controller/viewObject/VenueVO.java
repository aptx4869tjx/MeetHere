package com.tjx.MeetHere.controller.viewObject;

import java.time.LocalDate;

public class VenueVO {
    private Long venueId;

    private String name;

    private String description;

    private Double price;

    private String site;

    private LocalDate date;//日期

    private int[] timeSlots;//开放的所有时段,以24小时计数

    private int[] occupiedTimeSlots;//占用的时段

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int[] getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(int[] timeSlots) {
        this.timeSlots = timeSlots;
    }

    public int[] getOccupiedTimeSlots() {
        return occupiedTimeSlots;
    }

    public void setOccupiedTimeSlots(int[] occupiedTimeSlots) {
        this.occupiedTimeSlots = occupiedTimeSlots;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
